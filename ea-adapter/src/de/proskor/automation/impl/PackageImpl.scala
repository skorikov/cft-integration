package de.proskor.automation.impl

import de.proskor.automation._
import de.proskor.automation.impl.collections._
import cli.EA.IPackage
import cli.EA.ICollection
import cli.EA.IElement

class PackageImpl(peer: IPackage) extends Package {
  override def id: Int = peer.get_PackageID
  override def guid: String = peer.get_PackageGUID.asInstanceOf[String]

  override def name: String = peer.get_Name.asInstanceOf[String]
  override def name_=(name: String) {
    peer.set_Name(name)
    peer.Update()
    RepositoryImpl.peer.RefreshModelView(0)
  }

  override def description: String = peer.get_Notes.asInstanceOf[String]
  override def description_=(description: String) {
    peer.set_Notes(description)
    peer.Update()
  }

  override def element: Element = new ElementImpl(peer.get_Element.asInstanceOf[IElement])

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