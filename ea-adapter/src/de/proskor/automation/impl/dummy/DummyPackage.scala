package de.proskor.automation.impl.dummy

import de.proskor.automation.{Collection, Diagram, Element, Package}

class DummyPackage(val parent: Option[Package], val id: Int, var name: String) extends Package {
  lazy val packages: Collection[Package] = new DummyPackageCollection(this)
  def elements: Collection[Element] = null
  def diagrams: Collection[Diagram] = null
}