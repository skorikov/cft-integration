package de.proskor.automation

import cli.EA.IPackage
import cli.EA.ICollection

class Package(peer: IPackage) {
  def id: Int = peer.get_PackageID

  def name: String = peer.get_Name.asInstanceOf[String]
  def name_=(name: String): Unit = peer.set_Name(name)

  def packages: Collection[Package] = new PackageCollection(peer.get_Packages.asInstanceOf[ICollection])
  def elements: Collection[Element] = new ElementCollection(peer.get_Elements.asInstanceOf[ICollection])
  def diagrams: Collection[Diagram] = new DiagramCollection(peer.get_Diagrams.asInstanceOf[ICollection])

  def parent: Option[Package] =
    if (peer.get_ParentID > 0)
      Some(new Package(Repository.getPackageById(peer.get_ParentID)))
    else
      None
}