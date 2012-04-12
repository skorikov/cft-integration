package de.proskor.cft.model.ea

import de.proskor.cft.model.Event
import de.proskor.cft.model.Container
import de.proskor.cft.model.ea.peers.ElementPeer

class EAEvent(var peer: ElementPeer) extends ElementPeered with Event {
  def parent: Option[Container] = if (peer.isProxy) None else peer.parent.map(new EAComponent(_))
}