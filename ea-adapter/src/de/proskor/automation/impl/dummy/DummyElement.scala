package de.proskor.automation.impl.dummy

import de.proskor.automation.{Collection, Connector, Element, Package, TaggedValue}

class DummyElement(parent: Element, val pkg: Package, var name: String) extends Element {
  val id: Int = IdGenerator.next
  val guid: String = id.toString
  var stereotype: String = ""
  var author: String = ""
  var description: String = ""

  lazy val connectors: Collection[Connector] = new DummyConnectorCollection(this)

  lazy val elements: Collection[Element] = new DummyCollection(this,
    (name: String, typ: String, parent: Element) => new DummyElement(parent, pkg, name))

  lazy val taggedValues: Collection[TaggedValue] = new DummyCollection(this,
    (name: String, typ: String, parent: Element) => new DummyTaggedValue(parent, name))

  def parent: Option[Element] = Option(parent)
}