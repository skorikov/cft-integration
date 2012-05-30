package de.proskor.automation.impl.dummy

import de.proskor.automation.{Collection, Connector, Diagram, Element, Node, Package}

class DummyNode(val diagram: Diagram, val pkg: Package, var name: String) extends Node {
  val id: Int = IdGenerator.next
  var stereotype: String = ""

  var left: Int = 0
  var top: Int = 0
  var width: Int = 0
  var height: Int = 0
  var sequence: Int = 0

  var element: Element = null
}