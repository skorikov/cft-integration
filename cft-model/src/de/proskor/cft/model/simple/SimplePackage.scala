package de.proskor.cft.model.simple
import de.proskor.cft.model.{Component,Element,Package}

private class SimplePackage(initialName: String) extends SimpleContainer(initialName) with Package {
  def packages: Set[Package] = elements.filter(_.isInstanceOf[Package]).asInstanceOf[Set[Package]]
  def components: Set[Component] = elements.filter(_.isInstanceOf[Component]).asInstanceOf[Set[Component]]

  override def +=(element: Element) {
    require(element.isInstanceOf[SimplePackage] || element.isInstanceOf[SimpleComponent])
    super.+=(element)
  }
}