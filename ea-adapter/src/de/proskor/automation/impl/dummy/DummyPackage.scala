package de.proskor.automation.impl.dummy

import de.proskor.automation.{Collection, Diagram, Element, Package}

class DummyPackage(val parent: Option[Package], val id: Int, var name: String) extends Package {
  lazy val packages: Collection[Package] = new DummyPackageCollection(this)
  lazy val elements: Collection[Element] = new DummyCollection(this,
      (name: String, typ: String, parent: Package) => new DummyElement(null, parent, 0, name))
  def diagrams: Collection[Diagram] = null
}