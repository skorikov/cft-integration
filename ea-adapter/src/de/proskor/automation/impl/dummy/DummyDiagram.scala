package de.proskor.automation.impl.dummy

import de.proskor.automation.{Collection, Connector, Element, Package, Diagram, Node}

class DummyDiagram(var name: String) extends Diagram {
  val id: Int = IdGenerator.next
  var stereotype: String = ""
  def nodes: Collection[Node] = null
}