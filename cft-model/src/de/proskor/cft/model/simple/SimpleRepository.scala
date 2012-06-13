package de.proskor.cft.model.simple

import de.proskor.cft.model.{Container, Element, Package, Repository}

private class SimpleRepository(initialName: String) extends SimplePackage(initialName) with Repository {
  override val parent: Option[Package] = None

  override def add(element: Element) {
    require(element.isInstanceOf[SimplePackage])
    super.add(element)
  }
}