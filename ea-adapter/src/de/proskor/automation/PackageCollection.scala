package de.proskor.automation

import cli.EA.ICollection
import cli.EA.IPackage

class PackageCollection(peer: ICollection) extends Collection[Package](peer) {
  type PeerType = IPackage
  override def create(peer: IPackage): Package = new Package(peer)
  override def update(peer: IPackage): Unit = peer.Update()
}