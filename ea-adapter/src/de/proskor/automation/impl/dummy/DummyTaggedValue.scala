package de.proskor.automation.impl.dummy

import de.proskor.automation.{Collection, Connector, Element, Package, TaggedValue}

class DummyTaggedValue(parent: Element, var name: String) extends TaggedValue {
  var value = ""
  var description = ""
  val id: Int = IdGenerator.next
  val guid: String = id.toString
  val element: Element = parent
}