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
  def copyLeft(component: Component, target: Container, trace: MergeTrace): Component = {
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
      copyLeft(subcomponent, result, trace)
    }

    // connect ports
    for (port <- component.outports ++ component.components.flatten(_.inports)) {
      port.input foreach {
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

  def copyRight(component: Component, target: Container, trace: MergeTrace): Component = {
    val result = Component(target, component.name)
    trace.mapRight(component, result)

    // copy elements
    for (event <- component.events) {
      trace.mapRight(event, Event(result, event.name))
    }
    for (inport <- component.inports) {
      trace.mapRight(inport, Inport(result, inport.name))
    }
    for (outport <- component.outports) {
      trace.mapRight(outport, Outport(result, outport.name))
    }
    for (gate <- component.gates) gate match {
      case And(name, _) => trace.mapRight(gate, And(result, name))
      case Or(name, _) => trace.mapRight(gate, Or(result, name))
    }
    for (subcomponent <- component.components) {
      copyRight(subcomponent, result, trace)
    }

    // connect ports
    for (port <- component.outports ++ component.components.flatten(_.inports)) {
      port.input foreach {
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

  def mergeComponents(left: Component, right: Component, target: Container, trace: MergeTrace): Component = {
    val result = Component(target, left.name)
    trace.map(left, right, result)

    // merge events
    val (leftEvents, rightEvents, matchingEvents) = decompose(left.events, right.events)
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
    val (leftInports, rightInports, matchingInports) = decompose(left.inports, right.inports)
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
    val (leftOutports, rightOutports, matchingOutports) = decompose(left.outports, right.outports)
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
    val (leftGates, rightGates, matchingGates) = decompose(left.gates, right.gates)
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
    val (leftComponents, rightComponents, matchingComponents) = decompose(left.components, right.components)
    for (component <- leftComponents) {
      copyLeft(component, result, trace)
    }
    for (component <- rightComponents) {
      copyRight(component, result, trace)
    }
    for ((leftComponent, rightComponent) <- matchingComponents) {
      mergeComponents(leftComponent, rightComponent, result, trace)
    }

    // connect ports
    for (port <- result.outports ++ result.components.flatten(_.inports)) {
      trace.sources(port) match {
        case (Some(leftSource: Port), Some(rightSource: Port)) => leftSource.input foreach {
          case input => port += trace.target(input).asInstanceOf[Source]
        }
        case (Some(leftSource: Port), None) => leftSource.input foreach {
          case input => port += trace.target(input).asInstanceOf[Source]
        }
        case (None, Some(rightSource: Port)) => rightSource.input foreach {
          case input => port += trace.target(input).asInstanceOf[Source]
        }
        case _ => throw new IllegalStateException
      }
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

    result
  }

  private def decompose[T <: Element](left: Set[T], right: Set[T]): (Set[T], Set[T], Iterable[(T, T)]) = {
    val pairs = matchingPairs(left, right)
    val (singleLeft, singleRight) = singleElements(left, right, pairs)
    (singleLeft, singleRight, pairs)
  }

  private def matchingPairs[T <: Element](left: Set[T], right: Set[T]): Iterable[(T, T)] = for {
    leftElement <- left
    rightElement <- right
    if matches(leftElement, rightElement)
  } yield (leftElement, rightElement)

  private def matches(left: Set[Source], right: Set[Source]): Boolean =
    left.size == right.size &&
    left.forall(element => right.exists(matches(element, _))) &&
    right.forall(element => left.exists(matches(element, _)))

  private def matches(left: Element, right: Element): Boolean = (left.name == right.name) && (left match {
    case And(_, inputs) => right.isInstanceOf[And] && matches(inputs, right.asInstanceOf[Gate].inputs)
    case Or(_, inputs) => right.isInstanceOf[Or] && matches(inputs, right.asInstanceOf[Gate].inputs)
    case _ => true
  })

  private def singleElements[T <: Element](left: Set[T], right: Set[T], pairs: Iterable[(T, T)]): (Set[T], Set[T]) = {
    val singleLeft = left.filterNot(element => pairs.exists {
      case (left, _) => element == left
    })
    val singleRight = right.filterNot(element => pairs.exists {
      case (_, right) => element == right
    })
    (singleLeft, singleRight)
  }
}