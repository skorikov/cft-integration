package de.proskor.cft.model.ea

import de.proskor.cft.model.ea.peers.ElementPeer

trait ElementPeered {
  var peer: ElementPeer

  def name: String = peer.name
  def name_=(name: String) { peer.name = name }

  def elementsWithStereotype(stereotypes: String*): Set[ElementPeer] =
    peer.elements.filter(kid => stereotypes.contains(kid.stereotype))
}