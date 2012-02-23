package de.proskor.cft.merge
import de.proskor.cft.model.Component
import de.proskor.cft.model.Container
import de.proskor.cft.model.Event
import de.proskor.cft.model.Element
import de.proskor.cft.model.CftFactory
import de.proskor.cft.model.Inport
import de.proskor.cft.model.Source
import de.proskor.cft.model.Outport
import de.proskor.cft.model.Gate
import de.proskor.cft.model.Port
import de.proskor.cft.model.And
import de.proskor.cft.model.Or

class MergeAlgorithm {
  def merge[T <: Element](left: T, right: T, target: Container, trace: MergeTrace): T = (left, right, target) match {
    case (leftComponent: Component, rightComponent: Component, _) =>
      mergeComponents(leftComponent, rightComponent, target, trace).asInstanceOf[T]
    case (leftEvent: Event, rightEvent: Event, component: Component) =>
      mergeEvents(leftEvent, rightEvent, component, trace).asInstanceOf[T]
    case (leftInport: Inport, rightInport: Inport, component: Component) =>
      mergeInports(leftInport, rightInport, component, trace).asInstanceOf[T]
    case (leftOutport: Outport, rightOutport: Outport, component: Component) =>
      mergeOutports(leftOutport, rightOutport, component, trace).asInstanceOf[T]
    case (leftGate: Gate, rightGate: Gate, component: Component) =>
      mergeGates(leftGate, rightGate, component, trace).asInstanceOf[T]
  }

  def layers(elements: Set[Element]): Seq[Set[Element]] = {
    val groups = elements.groupBy(level(_, elements))
    for (i <- 0 until groups.keys.size) yield groups(i)
  }

  def level(element: Element, set: Set[Element]): Int = element match {
    case Gate(_, inputs) => level(inputs, set)
    case Component(_, _, inports, _, _, _) => level(inports.map(_.input.get), set)
  }

  def level(inputs: Set[Source], set: Set[Element]): Int = {
    val kids = set.intersect(inputs.map(target))
    if (kids.isEmpty) 0 else 1 + kids.map(level(_, set)).max
  }

  def target(element: Source): Element = element match {
    case gate: Gate => element
    case port: Port => port.parent.get
  }

  def mergeComponents(left: Component, right: Component, target: Container, trace: MergeTrace): Component = {
    val result = Component(target, left.name)
    trace.map(left, right, result)
    process(left.events, right.events, result, copyEvent(_: Event, result, trace), trace)
    process(left.inports, right.inports, result, copyInport(_: Inport, result, trace), trace)
    for ((leftLayer, rightLayer) <- layers(left.components ++ left.gates).zipAll(layers(right.components ++ right.gates), Set[Element](), Set[Element]())) {
      process(leftLayer, rightLayer, result, copyElement(_: Element, result, trace), trace)
    }
    process(left.outports, right.outports, result, copyOutport(_: Outport, result, trace), trace)
    result
  }

  def copyElement(element: Element, parent: Container, trace: MergeTrace): Element = element match {
    case Gate(name, inputs) => {
      val result = element match {
        case and: And => And(name)
        case or: Or => Or(name)
      }
      parent += result
      inputs.foreach(result += trace.target(_).asInstanceOf[Source])
      result
    }
    case Component(name, events, inports, outports, gates, components) => {
      val result = Component(name)
      parent += result
      for (event <- events) {
        trace.mapLeft(event, copyEvent(event, result, trace))
        
      }
      result
    }
  }

  def copyEvent(event: Event, parent: Container, trace: MergeTrace): Event = {
    val result = Event(event.name)
    parent += result
    result
  }

  def copyInport(inport: Inport, parent: Container, trace: MergeTrace): Inport = {
    val result = Inport(inport.name)
    parent += result
    inport.input foreach {
      case value => result += trace.target(value).asInstanceOf[Source]
    }
    result
  }

  def copyOutport(outport: Outport, parent: Container, trace: MergeTrace): Outport = {
    val result = Outport(outport.name)
    parent += result
    outport.input foreach {
      case value => result += trace.target(value).asInstanceOf[Source]
    }
    result
  }

  def mergeEvents(left: Event, right: Event, target: Component, trace: MergeTrace): Event = {
    val result = Event(target, left.name)
    trace.map(left, right, result)
    result
  }

  def mergeInports(left: Inport, right: Inport, target: Component, trace: MergeTrace): Inport = {
    val result = Inport(target, left.name)
    trace.map(left, right, result)
    result
  }

  def mergeOutports(left: Outport, right: Outport, target: Component, trace: MergeTrace): Outport = {
    val result = Outport(target, left.name)
    trace.map(left, right, result)
    result
  }

  def mergeGates(left: Gate, right: Gate, target: Component, trace: MergeTrace): Gate = {
    val result = left match {
      case and: And => And(target, left.name)
      case or: Or => Or(target, left.name)
    }
    trace.map(left, right, result)
    result
  }

  private def process[T <: Element](left: Set[T], right: Set[T], target: Container, constructor: T => T, trace: MergeTrace) {
    val (leftElements, rightElements, matchingElements) = decompose(left, right)
    for (element <- leftElements) {
      val copy = constructor(element)
      target += copy
      trace.mapLeft(element, copy)
    }
    for (element <- rightElements) {
      val copy = constructor(element)
      target += copy
      trace.mapRight(element, copy)
    }
    for ((left, right) <- matchingElements) {
      merge(left, right, target, trace)
    }
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

  private def matches(left: Element, right: Element): Boolean = left.name == right.name

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