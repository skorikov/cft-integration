package de.proskor.cft.model.ea
import de.proskor.cft.model._
import de.proskor.cft.model.ea.peers._

private class EAElement(var peer: EAPeer) extends Element {
  def id: Int = peer.id
  
  def name: String = peer.name
  def name_=(name: String) {
    peer.name = name
  }

  def parent: Option[Container] = peer match {
    case peer: EAProxyPeer => None
    case _ => Some(EAFactory.create(peer.parent).asInstanceOf[Container]) 
  }

  override def equals(that: Any): Boolean = that match {
    case element: EAElement => id == element.id
    case _ => false
  }

  override def hashCode: Int = id
}