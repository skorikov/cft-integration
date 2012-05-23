package de.proskor.cft.model.ea.peers

trait Peered[PeerType <: Peer] {
  var peer: PeerType
}