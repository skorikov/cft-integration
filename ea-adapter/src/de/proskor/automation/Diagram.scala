package de.proskor.automation

import de.proskor.automation.collections._
import cli.EA.ICollection
import cli.EA.IDiagram

class Diagram(peer: IDiagram) {
  def id: Int = peer.get_DiagramID

  def name: String = peer.get_Name.asInstanceOf[String]
  def name_=(name: String): Unit = peer.set_Name(name)

  def stereotype: String = peer.get_Stereotype.asInstanceOf[String]
  def stereotype_(stereotype: String): Unit = peer.set_Stereotype(stereotype)

  def nodes: Collection[Node] = new NodeCollection(peer.get_DiagramObjects.asInstanceOf[ICollection])

  override def equals(that: Any): Boolean = that match {
    case diagram: Diagram => id == diagram.id
    case _ => false
  }

  override def hashCode: Int = id
}