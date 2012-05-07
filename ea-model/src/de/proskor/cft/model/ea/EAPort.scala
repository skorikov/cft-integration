package de.proskor.cft.model.ea

import de.proskor.cft.model.{Component, Port, Source}
import de.proskor.cft.model.ea.peers.ElementPeer

abstract class EAPort extends ElementPeered with Port {
  override def peer: ElementPeer
  override def parent: Option[Component] = peer.parent.map(new EAComponent(_))
  override def inputs: Set[Source] = Set()
  override def add(input: Source) {
    peer.connect(input.asInstanceOf[ElementPeered].peer)
  }
  override def remove(input: Source) {
    peer.disconnect(input.asInstanceOf[ElementPeered].peer)
  }
}