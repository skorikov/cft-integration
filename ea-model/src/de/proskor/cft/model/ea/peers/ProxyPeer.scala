package de.proskor.cft.model.ea.peers

trait ProxyPeer extends ElementPeer with PackagePeer {
  override val parent: Option[ElementPeer] = None
  override val pkg: Option[PackagePeer] = None
}