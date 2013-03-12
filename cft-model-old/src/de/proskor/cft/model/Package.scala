package de.proskor.cft.model

trait Package extends Container {
  override def parent: Option[Package]
  def packages: Set[Package]
  def components: Set[Component]
}

object Package {
  def apply(name: String): Package = Factory.createPackage(name)
  def apply(parent: Package, name: String): Package = Factory.createPackage(parent, name)
  def unapply(pkg: Package): Option[(String, Set[Package], Set[Component])] = Some(pkg.name, pkg.packages, pkg.components)
}