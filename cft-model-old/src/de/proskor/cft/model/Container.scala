package de.proskor.cft.model

trait Container extends Element {
  def elements: Set[Element]
  def add(element: Element): Unit
  def remove(element: Element): Unit
  final def +=(element: Element): Unit = add(element)
  final def -=(element: Element): Unit = remove(element)
}

object Container {
  def unapply(container: Container): Option[(String, Set[Element])] = Some(container.name, container.elements)
}