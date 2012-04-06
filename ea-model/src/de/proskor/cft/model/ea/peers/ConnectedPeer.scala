package de.proskor.cft.model.ea.peers

trait ConnectedPeer extends Peer {
  def connectedElements: Set[Peer]
  def connect(element: Peer): Unit
  def disconnect(element: Peer): Unit
}