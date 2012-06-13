package de.proskor.cft.model.ea.peers

trait ElementPeer extends Peer {
  def id: Int
  var name: String
  var stereotype: String
  def parent: Option[ElementPeer]
  def pkg: Option[PackagePeer]
  def elements: Set[ElementPeer]
  def connect(element: ElementPeer): Unit
  def disconnect(element: ElementPeer): Unit
  def inputs: Set[ElementPeer]
  def add(element: ElementPeer): ElementPeer
  def remove(element: ElementPeer): ElementPeer
}