package de.proskor.cft.model.ea

import de.proskor.cft.model.{Component, Gate, Source}
import de.proskor.cft.model.ea.peers.{ElementPeer, ElementPeered, ProxyPeer}

abstract class EAGate extends ElementPeered with Gate {
  override def hashCode: Int = peer.id

  def name: String = peer.name
  def name_=(name: String) { peer.name = name }

  override def peer: ElementPeer
  override def parent: Option[Component] = if (peer.isInstanceOf[ProxyPeer]) None else peer.parent.map(new EAComponent(_))
  override def inputs: Set[Source] = for {
    peer <- peer.inputs
    stereotype = peer.stereotype
  } yield stereotype match {
    case "Event" => new EAEvent(peer)
    case "AND" => new EAAnd(peer)
    case "OR" => new EAOr(peer)
    case "Input" => new EAInport(peer)
    case "Output" => new EAOutport(peer)
  }
  override def add(input: Source) {
    peer.connect(input.asInstanceOf[ElementPeered].peer)
  }
  override def remove(input: Source) {
    peer.disconnect(input.asInstanceOf[ElementPeered].peer)
  }
}