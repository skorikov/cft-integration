package de.proskor.cft.model.simple
import de.proskor.cft.model.{Container,Element,Repository}

private class SimpleRepository(initialName: String) extends SimplePackage(initialName) with Repository {
  override val parent: Option[Container] = None
  override def add(element: Element) {
    require(element.isInstanceOf[SimplePackage])
    super.add(element)
  }
}