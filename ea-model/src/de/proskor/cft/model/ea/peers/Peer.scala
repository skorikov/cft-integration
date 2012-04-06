package de.proskor.cft.model.ea.peers

trait Peer {
  def id: Int
  var name: String
  var stereotype: String
  def parent: ContainerPeer
}