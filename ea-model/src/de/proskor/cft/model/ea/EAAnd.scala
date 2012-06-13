package de.proskor.cft.model.ea

import de.proskor.cft.model.And
import de.proskor.cft.model.ea.peers.ElementPeer

class EAAnd(var peer: ElementPeer) extends EAGate with And {
  override def equals(that: Any): Boolean = that match {
    case event: EAAnd => event.peer.id == peer.id
    case _ => false
  }
}