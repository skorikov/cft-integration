package de.proskor.automation.impl.dummy

import de.proskor.automation.{Collection, Package, Repository}

object DummyRepository extends Repository {
  lazy val models: Collection[Package] = new DummyCollection(this,
      (name: String, typ: String, parent: Repository) => new DummyPackage(None, name))
}