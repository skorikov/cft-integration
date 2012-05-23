package de.proskor.cft.model.ea.peers

trait PackagePeer extends Peer {
  def id: Int
  var name: String
  def elements: Set[ElementPeer]
  def packages: Set[PackagePeer]
  def container: Option[PackagePeer]
  def add(element: ElementPeer): ElementPeer
  def remove(element: ElementPeer): ElementPeer
  def add(element: PackagePeer): PackagePeer
  def remove(element: PackagePeer): PackagePeer
}