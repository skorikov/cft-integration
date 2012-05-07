package de.proskor.cft.model.ea

import de.proskor.cft.model.ea.peers.PackagePeer

trait PackagePeered {
  var peer: PackagePeer

  def name: String = peer.name
  def name_=(name: String) { peer.name = name }
}