package de.proskor.cft.model.ea.peers

trait PackagePeer extends Peer {
  def elements: Set[ElementPeer]
  def packages: Set[PackagePeer]
  def container: Option[PackagePeer]
}