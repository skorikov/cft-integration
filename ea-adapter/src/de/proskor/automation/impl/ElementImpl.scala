package de.proskor.automation.impl

import de.proskor.automation._
import de.proskor.automation.impl.collections._
import cli.EA.IElement
import cli.EA.ICollection

class ElementImpl(peer: IElement) extends Element {
  override def id: Int = peer.get_ElementID
  override def guid: String = peer.get_ElementGUID.asInstanceOf[String]

  override def name: String = peer.get_Name.asInstanceOf[String]
  override def name_=(name: String) {
    peer.set_Name(name)
    peer.Update()
  }

  override def stereotype: String = peer.get_Stereotype.asInstanceOf[String]
  override def stereotype_=(stereotype: String) {
    peer.set_Stereotype(stereotype)
    peer.Update()
  }

  override def author: String = peer.get_Author.asInstanceOf[String]
  override def author_=(author: String) {
    peer.set_Author(author)
    peer.Update()
  }

  override def description: String = peer.get_Notes.asInstanceOf[String]
  override def description_=(description: String) {
    peer.set_Notes(description)
    peer.Update()
  }

  override def elements: Collection[Element] = new ElementCollection(peer.get_Elements.asInstanceOf[ICollection])
  override def connectors: Collection[Connector] = new ConnectorCollection(peer.get_Connectors.asInstanceOf[ICollection])
  override def taggedValues: Collection[TaggedValue] = new TaggedValueCollection(peer.get_TaggedValues.asInstanceOf[ICollection])

  override def parent: Option[Element] =
    if (peer.get_ParentID > 0)
      Some(new ElementImpl(RepositoryImpl.getElementById(peer.get_ParentID)))
    else
      None

  override def pkg: Package = new PackageImpl(RepositoryImpl.getPackageById(peer.get_PackageID))

  override def equals(that: Any): Boolean = that match {
    case element: Element => id == element.id
    case _ => false
  }

  override def hashCode: Int = id
}