package de.proskor.cft.model.ea

import de.proskor.cft.model._
import de.proskor.cft.model.ea.peers._

trait EAElementTrait[T <: Peer] {
  var peer: T

  def id: Int = peer.id
  
  def name: String = peer.name
  def name_=(name: String) {
    peer.name = name
  }

  def parent: Option[Container] = peer match {
    case peer: EAProxyPeer => None
    case _ => Some(EAFactory.create(peer.parent).asInstanceOf[Container]) 
  }
}