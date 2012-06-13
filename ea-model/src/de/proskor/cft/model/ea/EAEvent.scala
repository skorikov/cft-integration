package de.proskor.cft.model.ea

import de.proskor.cft.model.{Component, Event}
import de.proskor.cft.model.ea.peers.ElementPeer
import de.proskor.cft.model.ea.peers.ElementPeered

class EAEvent(var peer: ElementPeer) extends ElementPeered with Event {
  override def equals(that: Any): Boolean = that match {
    case event: EAEvent => event.peer.id == peer.id
    case _ => false
  }

  override def hashCode: Int = peer.id

  def name: String = peer.name
  def name_=(name: String) { peer.name = name }

  def parent: Option[Component] = peer.parent.map(new EAComponent(_))
}