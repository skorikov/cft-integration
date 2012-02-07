package de.proskor.core
import de.proskor.model.cft.And
import de.proskor.model.cft.Or
import de.proskor.model.Element
import de.proskor.model.cft.Gate
import de.proskor.model.cft.Source
import de.proskor.model.cft.Component
import de.proskor.model.Repository

object MergeAlgorithm {
  var repository: Repository = null

  private def targets(source: Element, target: Element): Boolean = source match {
    case source: Source => targets(source, target)
    case component: Component => targets(component, target)
    case _ => false
  }
  private def targets(source: Source, target: Element): Boolean = (source == target) || source.targets.exists(targets(_, target))
  private def targets(source: Source, target: Component): Boolean = target.inputs.exists(targets(source, _))
  private def targets(source: Component, target: Element): Boolean = source.outputs.exists(targets(_, target))

  private def linearize(elements: Seq[Element]) = elements.sortWith(targets)

  def copyComponent(component: Component, target: Component, parentMap: Map[Source, Source]): (Map[Source, Source], Map[Component, Component]) = {
    var map = parentMap
    var componentMap = Map[Component, Component]()
    val result = target.addComponent
    result.name = component.name
    componentMap += (component -> result)

    // EVENTS
    for (event <- component.events) {
      val copy = result.addEvent
      copy.name = event.name
      map += (event -> copy)
    }

    // INPUTS
    for (input <- component.inputs) {
      val copy = result.addInput
      copy.name = input.name
      input.source match {
        case Some(source: Source) => copy += map(source)
        case None =>
      }
      map += (input -> copy)
    }

    // GATES & COMPONENTS
    for (element <- linearize(component.gates ::: component.components)) {
      element match {
        case gate: Gate => {
          val copy = gate match {
            case or: Or => result.addOr
            case and: And => result.addAnd
          }
          copy.name = element.name
          for (source <- gate.sources) {
	    	copy += map(source)
	      }
	      map += (gate -> copy)
        }
        case component: Component => {
          val copyMap = copyComponent(component, result, map)
          map ++= copyMap._1
          componentMap ++= copyMap._2
        //  map ++= copyComponent(component, result, map)
        }
      }
    }

    // OUTPUTS
    for (output <- component.outputs) {
      val copy = result.addOutput
      copy.name = output.name
      output.source match {
        case Some(source: Source) => copy += map(source)
        case None =>
      }
      map += (output -> copy)
    }

    (map, componentMap)
  }

  def merge(left: Component, right: Component, target: { def addComponent: Component }, parentMap: Map[Source, Source] = Map()): (Map[Source, Source], Map[Component, Component]) = {
    var map = parentMap
    var componentMap = Map[Component, Component]()
  //  repository.write("merging " + left.name + " with " + right.name)
    val result = target.addComponent
    result.name = left.name// + "-merged-with-" + right.name
    componentMap += (left -> result)
    componentMap += (right -> result)

    // EVENTS
    for (event <- left.events) {
      val copy = result.addEvent
      copy.name = event.name
      map += (event -> copy)
    }
    for (event <- right.events) {
      result.events.find(event matches _) match {
        case Some(copy) => map += (event -> copy)
        case None => {
          val copy = result.addEvent
          copy.name = event.name
          map += (event -> copy)
        }
      }
    }
  //  repository.write("events done")

    // INPUTS
    for (input <- left.inputs) {
      val copy = result.addInput
      copy.name = input.name
      input.source match {
        case Some(source: Source) => copy += map(source)
        case None =>
      }
      map += (input -> copy)
    }
    for (input <- right.inputs) {
      result.inputs.find(input matches _) match {
        case Some(copy) => {
          // MERGE
        //  repository.write("merging input " + input.elementName)
          copy.source match {
            case None => input.source match {
              case Some(source: Source) => copy += map(source)
              case None =>
            }
            case Some(source) => input.source match {
              case Some(that: Source) if (source != map(that)) => repository.write("conflict detected: " + copy.elementName + " is already connected")
              case None =>
            }
          }
          map += (input -> copy)
        }
        case None => {
          val copy = result.addInput
          copy.name = input.name
          map += (input -> copy)
          input.source match {
            case Some(source: Source) => copy += map(source)
            case None =>
          }
        }
      }
    }
  //  repository.write("inputs done")

  //  for (element <- linearize(left.gates ::: left.components)) {
  //    repository.write(element.name)
  //  }

    // GATES & COMPONENTS
    for (element <- linearize(left.gates ::: left.components)) {
      element match {
        case gate: Gate => {
          val copy = gate match {
            case or: Or => result.addOr
            case and: And => result.addAnd
          }
          copy.name = element.name
          for (source <- gate.sources) {
	    	copy += map(source)
	      }
	      map += (gate -> copy)
        }
        case component: Component => {
          right.components.find(component matches _) match {
          //  case None => map ++= merge(component, component, result, map)
            case None => {
              val copyMap = copyComponent(component, result, map)
              map ++= copyMap._1
          	  componentMap ++= copyMap._2
            //  map ++= copyComponent(component, result, map)
            }
            case Some(that: Component) => {
              val mergeMap = merge(component, that, result, map)
              map ++= mergeMap._1
          	  componentMap ++= mergeMap._2
            //  map ++= merge(component, that, result, map)
            }
          }
        }
      }
    }
    for (element <- linearize(right.gates ::: right.components.filterNot(c => result.components.exists(c matches _)))) {
      element match {
        case gate: Gate => {
          result.gates.find(gate matches _) match {
	        case Some(copy) => {
	          // MERGE
	          for (source <- gate.sources) {
	            copy.sources.find(source matches _) match {
	              case None => copy += map(source)
	              case Some(cs: Source) =>
	            }
		      }
	          map += (gate -> copy)
	        }
	        case None => {
	          val copy = gate match {
		        case or: Or => result.addOr
		        case and: And => result.addAnd
		      }
	          copy.name = gate.name
	          for (source <- gate.sources) {
		    	copy += map(source)
		      }
	          map += (gate -> copy)
	        }
	      }
        }
      //  case component: Component => map ++= merge(component, component, result, map)
        case component: Component => {
          val copyMap = copyComponent(component, result, map)
          map ++= copyMap._1
          componentMap ++= copyMap._2
        //  map ++= copyComponent(component, result, map)
        }
      }
    }
  //  repository.write("gates done")

    // OUTPUTS
    for (output <- left.outputs) {
      val copy = result.addOutput
      copy.name = output.name
      output.source match {
        case Some(source: Source) => copy += map(source)
        case None =>
      }
      map += (output -> copy)
    }
    for (output <- right.outputs) {
      result.outputs.find(output matches _) match {
        case Some(copy) => {
          // MERGE
          copy.source match {
            case None => output.source match {
              case Some(source: Source) => copy += map(source)
              case None =>
            }
            case Some(source) => output.source match {
              case Some(that: Source) if (source != map(that)) => repository.write("conflict detected: " + copy.elementName + " is already connected")
              case None =>
            }
          }
          map += (output -> copy)
        }
        case None => {
          val copy = result.addOutput
          copy.name = output.name
          map += (output -> copy)
          output.source match {
            case Some(source: Source) => copy += map(source)
            case None =>
          }
        }
      }
    }

  //  for (line <- result.toString.split("\n")) {
  //    repository.write(line)
  //  }
  //  repository.write("done")
    (map, componentMap)
  }
}