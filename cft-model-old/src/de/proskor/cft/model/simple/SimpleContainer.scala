package de.proskor.cft.model.simple

import de.proskor.cft.model.{Container, Element}

private abstract class SimpleContainer(initialName: String) extends SimpleElement(initialName) with Container {
  private var kids: Set[Element] = Set()

  def elements: Set[Element] = kids

  def add(element: Element) {
    require(element.isInstanceOf[SimpleElement])
    element.parent foreach {
      case container => container -= element
    }
    element.asInstanceOf[SimpleElement].container = Some(this)
    kids += element
  }

  def remove(element: Element) {
    require(element.isInstanceOf[SimpleElement] && kids.contains(element))
    element.asInstanceOf[SimpleElement].container = None
    kids -= element
  }
}