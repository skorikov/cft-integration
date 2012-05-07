package de.proskor.automation.impl.dummy

import de.proskor.automation.{Collection, Package, Repository}

object DummyRepository extends Repository {
  override lazy val models: Collection[Package] = new DummyPackageCollection(null)
}