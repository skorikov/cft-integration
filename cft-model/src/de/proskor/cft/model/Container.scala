package de.proskor.cft.model

trait Container extends Element {
  def elements: Set[Element]
  def +=(element: Element): Unit
  def -=(element: Element): Unit
}