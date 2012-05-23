package de.proskor.cft.model.ea

import de.proskor.cft.model.Or
import de.proskor.cft.model.ea.peers.ElementPeer

class EAOr(var peer: ElementPeer) extends EAGate with Or {
  override def equals(that: Any): Boolean = that match {
    case event: EAOr => event.peer.id == peer.id
    case _ => false
  }
}