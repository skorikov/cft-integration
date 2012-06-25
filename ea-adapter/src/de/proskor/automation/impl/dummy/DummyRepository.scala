package de.proskor.automation.impl.dummy

import de.proskor.automation.{Collection, Diagram, Identity, Package, Repository}

object DummyRepository extends Repository {
  override lazy val models: Collection[Package] = new DummyCollection(this,
      (name: String, typ: String, parent: Repository) => new DummyPackage(None, name))

  override lazy val context: Option[Identity] = None

  override lazy val diagram: Option[Diagram] = None

  override def write(text: String) = println(text)

  override val toString: String = "/"
}