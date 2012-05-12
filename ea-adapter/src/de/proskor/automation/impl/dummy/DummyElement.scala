package de.proskor.automation.impl.dummy

import de.proskor.automation.{Collection, Connector, Element, Package}

class DummyElement(parent: Element, itspkg: Package, val id: Int, var name: String) extends Element {
  var stereotype: String = ""
  def connectors: Collection[Connector] = null
  def elements: Collection[Element] = null
  def parent: Option[Element] = Option(parent)
  def pkg: Package = itspkg
}