package de.proskor.automation.impl.dummy

import de.proskor.automation.{Collection, Connector, Element, Package, Diagram, Node}

class DummyDiagram(var name: String) extends Diagram {
  val id: Int = IdGenerator.next
  val guid: String = id.toString
  var description: String = ""
  var stereotype: String = ""
  def nodes: Collection[Node] = new DummyCollection(this,
    (name: String, typ: String, parent: Diagram) => new DummyNode(parent, null, name))
}