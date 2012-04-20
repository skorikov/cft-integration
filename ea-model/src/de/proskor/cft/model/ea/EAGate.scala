package de.proskor.cft.model.ea

import de.proskor.cft.model.{Component, Gate, Source}
import de.proskor.cft.model.ea.peers.ElementPeer

abstract class EAGate extends ElementPeered with Gate {
  override def peer: ElementPeer
  override def parent: Option[Component] = if (peer.isProxy) None else peer.parent.map(new EAComponent(_))
  override def inputs: Set[Source] = Set()
  override def add(input: Source) {}
  override def remove(input: Source) {}
}