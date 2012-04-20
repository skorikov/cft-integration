package de.proskor.cft.merge
import de.proskor.cft.model.Component
import de.proskor.cft.model.Container
import de.proskor.cft.model.Event
import de.proskor.cft.model.Element
import de.proskor.cft.model.Inport
import de.proskor.cft.model.Source
import de.proskor.cft.model.Outport
import de.proskor.cft.model.Gate
import de.proskor.cft.model.Port
import de.proskor.cft.model.And
import de.proskor.cft.model.Or

class MergeAlgorithm {

  def copy(component: Component, target: Container, trace: MergeTrace, map: (Element, Element) => Unit): Component = {
    val result = Component(target, component.name)
    trace.mapLeft(component, result)

    // copy elements
    for (event <- component.events) {
      trace.mapLeft(event, Event(result, event.name))
    }
    for (inport <- component.inports) {
      trace.mapLeft(inport, Inport(result, inport.name))
    }
    for (outport <- component.outports) {
      trace.mapLeft(outport, Outport(result, outport.name))
    }
    for (gate <- component.gates) gate match {
      case And(name, _) => trace.mapLeft(gate, And(result, name))
      case Or(name, _) => trace.mapLeft(gate, Or(result, name))
    }
    for (subcomponent <- component.components) {
      copy(subcomponent, result, trace, map)
    }

    // connect ports
    for (port <- component.outports ++ component.components.flatten(_.inports)) {
      port.inputs foreach {
        case input => trace.target(port).asInstanceOf[Port] += trace.target(input).asInstanceOf[Source]
      }

    // connect gates
      for (gate <- component.gates) {
        for (input <- gate.inputs) {
          trace.target(gate).asInstanceOf[Gate] += trace.target(input).asInstanceOf[Source]
        }
      }
    }

    result
  }

  def merge(left: Component, right: Component, target: Container, trace: MergeTrace): Component = {
    val result = Component(target, left.name)
    trace.map(left, right, result)

    // merge events
    val (leftEvents, matchingEvents, rightEvents) = decompose(left.events, right.events)
    for (event <- leftEvents) {
      trace.mapLeft(event, Event(result, event.name))
    }
    for (event <- rightEvents) {
      trace.mapRight(event, Event(result, event.name))
    }
    for ((leftEvent, rightEvent) <- matchingEvents) {
      trace.map(leftEvent, rightEvent, Event(result, leftEvent.name))
    }

    // merge inports
    val (leftInports, matchingInports, rightInports) = decompose(left.inports, right.inports)
    for (inport <- leftInports) {
      trace.mapLeft(inport, Inport(result, inport.name))
    }
    for (inport <- rightInports) {
      trace.mapRight(inport, Inport(result, inport.name))
    }
    for ((leftInport, rightInport) <- matchingInports) {
      trace.map(leftInport, rightInport, Inport(result, leftInport.name))
    }

    // merge outports
    val (leftOutports, matchingOutports, rightOutports) = decompose(left.outports, right.outports)
    for (outport <- leftOutports) {
      trace.mapLeft(outport, Outport(result, outport.name))
    }
    for (outport <- rightOutports) {
      trace.mapRight(outport, Outport(result, outport.name))
    }
    for ((leftOutport, rightOutport) <- matchingOutports) {
      trace.map(leftOutport, rightOutport, Outport(result, leftOutport.name))
    }

    // merge gates
    val (leftGates, matchingGates, rightGates) = decompose(left.gates, right.gates)
    for (gate <- leftGates) {
      trace.mapLeft(gate, gate match {
        case And(name, _) => And(result, name)
        case Or(name, _) => Or(result, name)
      })
    }
    for (gate <- rightGates) {
      trace.mapRight(gate, gate match {
        case And(name, _) => And(result, name)
        case Or(name, _) => Or(result, name)
      })
    }
    for ((leftGate, rightGate) <- matchingGates) {
      trace.map(leftGate, rightGate, leftGate match {
        case And(name, _) => And(result, name)
        case Or(name, _) => Or(result, name)
      })
    }

    // merge components
    val (leftComponents, matchingComponents, rightComponents) = decompose(left.components, right.components)
    for (component <- leftComponents) {
      copy(component, result, trace, trace.mapLeft(_, _))
    }
    for (component <- rightComponents) {
      copy(component, result, trace, trace.mapRight(_, _))
    }
    for ((leftComponent, rightComponent) <- matchingComponents) {
      merge(leftComponent, rightComponent, result, trace)
    }

    // connect gates
    for (gate <- result.gates) {
      trace.sources(gate) match {
        case (Some(leftSource: Gate), Some(rightSource: Gate)) => leftSource.inputs foreach {
          gate += trace.target(_).asInstanceOf[Source]
        }
        case (Some(leftSource: Gate), None) => leftSource.inputs foreach {
          gate += trace.target(_).asInstanceOf[Source]
        }
        case (None, Some(rightSource: Gate)) => rightSource.inputs foreach {
          gate += trace.target(_).asInstanceOf[Source]
        }
        case _ => throw new IllegalStateException
      }
    }

    // connect ports, detect conflicts
    for (port <- result.outports ++ result.components.flatten(_.inports)) {
      trace.sources(port) match {
        case (Some(leftSource: Port), Some(rightSource: Port)) => {
          leftSource.inputs ++ rightSource.inputs foreach {
            case input => port += trace.target(input).asInstanceOf[Source]
          }
        /*  leftSource.input match {
            case None => rightSource.input match {
              case None =>
              case Some(input) => port += trace.target(input).asInstanceOf[Source]
            }
            case Some(input) => rightSource.input match {
              case None => port += trace.target(input).asInstanceOf[Source]
              case Some(other) => if (elementMatching(input, other))
                port += trace.target(input).asInstanceOf[Source]
              else
                trace.conflict(input, other)
            }
          }*/
        }
        case (Some(leftSource: Port), None) => leftSource.inputs foreach {
          case input => port += trace.target(input).asInstanceOf[Source]
        }
        case (None, Some(rightSource: Port)) => rightSource.inputs foreach {
          case input => port += trace.target(input).asInstanceOf[Source]
        }
        case _ => throw new IllegalStateException
      }
    }

    result
  }

  private def decompose[T](left: Set[T], right: Set[T])(implicit matching: (T, T) => Boolean): (Set[T], Set[(T, T)], Set[T]) = {
    val matchingPairs = for {
      leftElement <- left
      rightElement <- right
      if (matching(leftElement, rightElement))
    } yield (leftElement, rightElement)

    val leftSingles = left.filterNot(element => matchingPairs.exists {
      case (left, _) => element == left
    })

    val rightSingles = right.filterNot(element => matchingPairs.exists {
      case (_, right) => element == right
    })

    (leftSingles, matchingPairs, rightSingles)
  }

  private def setMatching[T](left: Set[T], right: Set[T])(implicit matching: (T, T) => Boolean): Boolean =
    left.size == right.size &&
    left.forall(element => right.exists(matching(element, _))) &&
    right.forall(element => left.exists(matching(element, _)))

  private implicit def elementMatching(left: Element, right: Element): Boolean = (left.name == right.name) && (left match {
    case And(_, inputs) => right.isInstanceOf[And] && setMatching(inputs, right.asInstanceOf[Gate].inputs)
    case Or(_, inputs) => right.isInstanceOf[Or] && setMatching(inputs, right.asInstanceOf[Gate].inputs)
    case _ => true
  })
}