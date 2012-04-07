package de.proskor.automation.impl

import de.proskor.automation._
import de.proskor.automation.impl.collections._
import cli.EA.IPackage
import cli.EA.ICollection

class PackageImpl(peer: IPackage) extends Package {
  override def id: Int = peer.get_PackageID

  override def name: String = peer.get_Name.asInstanceOf[String]
  override def name_=(name: String): Unit = peer.set_Name(name)

  override def packages: Collection[Package] = new PackageCollection(peer.get_Packages.asInstanceOf[ICollection])
  override def elements: Collection[Element] = new ElementCollection(peer.get_Elements.asInstanceOf[ICollection])
  override def diagrams: Collection[Diagram] = new DiagramCollection(peer.get_Diagrams.asInstanceOf[ICollection])

  override def parent: Option[Package] =
    if (peer.get_ParentID > 0)
      Some(new PackageImpl(RepositoryImpl.getPackageById(peer.get_ParentID)))
    else
      None

  override def equals(that: Any): Boolean = that match {
    case pkg: Package => id == pkg.id
    case _ => false
  }

  override def hashCode: Int = id
}