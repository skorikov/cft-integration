package de.proskor.automation.impl.dummy

import de.proskor.automation.{Collection, Diagram, Element, Package}

class DummyPackage(val parent: Option[Package], var name: String) extends Package {
  val id: Int = IdGenerator.next
  lazy val packages: Collection[Package] = new DummyCollection(this,
      (name: String, typ: String, parent: Package) => new DummyPackage(Some(parent), name))
  lazy val elements: Collection[Element] = new DummyCollection(this,
      (name: String, typ: String, parent: Package) => new DummyElement(null, parent, name))
  lazy val diagrams: Collection[Diagram] = new DummyCollection(this,
      (name: String, typ: String, parent: Package) => new DummyDiagram(name))
}