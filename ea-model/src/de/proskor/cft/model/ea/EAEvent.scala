package de.proskor.cft.model.ea

import de.proskor.cft.model.{Component, Event}
import de.proskor.cft.model.ea.peers.ElementPeer

class EAEvent(var peer: ElementPeer) extends ElementPeered with Event {
  def parent: Option[Component] = peer.parent.map(new EAComponent(_))
}