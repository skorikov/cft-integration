package de.proskor.cft.model

trait Component extends Container {
  def events: Set[Event]
  def inports: Set[Inport]
  def outports: Set[Outport]
  def gates: Set[Gate]
  def components: Set[Component]
}

object Component {
  def apply(name: String): Component = Factory.createComponent(name)
  def apply(parent: Container, name: String): Component = Factory.createComponent(parent, name)
  def unapply(component: Component): Option[(String, Set[Event], Set[Inport], Set[Outport], Set[Gate], Set[Component])] =
    Some(component.name, component.events, component.inports, component.outports, component.gates, component.components)
}