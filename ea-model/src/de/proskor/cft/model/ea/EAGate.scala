package de.proskor.cft.model.ea
import de.proskor.cft.model.ea.peers.EAPeer
import de.proskor.cft.model.Source
import de.proskor.cft.model.Gate

private class EAGate(initialPeer: EAPeer) extends EAElement(initialPeer) with Gate {
  def inputs: Set[Source] = peer.connectedElements.map(EAFactory.create).asInstanceOf[Set[Source]]
  def add(input: Source) {
    require(input.isInstanceOf[EAElement])
    peer.connect(input.asInstanceOf[EAElement].peer)
  }
  def remove(input: Source) {
    require(input.isInstanceOf[EAElement])
    peer.disconnect(input.asInstanceOf[EAElement].peer)
  }
}