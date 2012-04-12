package de.proskor.cft.model.ea.peers

trait Peer {
  def id: Int
  var name: String
  var stereotype: String
  val isProxy: Boolean
//  def add[T <: Peer](peer: T): T
//  def remove[T <: Peer](peer: T): T
}