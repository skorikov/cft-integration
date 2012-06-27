package de.proskor.automation.impl

import de.proskor.automation._
import de.proskor.automation.impl.collections._
import cli.EA.ICollection
import cli.EA.IDiagram

class DiagramImpl(peer: IDiagram) extends Diagram {
  override def id: Int = peer.get_DiagramID
  override def guid: String = peer.get_DiagramGUID.asInstanceOf[String]

  override def name: String = peer.get_Name.asInstanceOf[String]
  override def name_=(name: String): Unit = peer.set_Name(name)

  override def description: String = peer.get_Notes.asInstanceOf[String]
  override def description_=(description: String) {
    peer.set_Notes(description)
    peer.Update()
  }

  override def stereotype: String = peer.get_Stereotype.asInstanceOf[String]
  override def stereotype_=(stereotype: String): Unit = peer.set_Stereotype(stereotype)

  override def nodes: Collection[Node] = new NodeCollection(peer.get_DiagramObjects.asInstanceOf[ICollection])

  override def equals(that: Any): Boolean = that match {
    case diagram: Diagram => id == diagram.id
    case _ => false
  }

  override def hashCode: Int = id
}