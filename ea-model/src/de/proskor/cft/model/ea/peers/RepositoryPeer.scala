package de.proskor.cft.model.ea.peers

trait RepositoryPeer extends Peer {
  def packages: Set[PackagePeer]
}

object RepositoryPeer {
  def instance: RepositoryPeer = null
}