package de.proskor.automation.impl.collections

import de.proskor.automation.Package
import de.proskor.automation.impl.PackageImpl
import cli.EA.ICollection
import cli.EA.IPackage

private[automation] class PackageCollection(peer: ICollection) extends AbstractCollection[Package](peer) {
  type PeerType = IPackage
  override def create(peer: IPackage): Package = new PackageImpl(peer)
  override def update(peer: IPackage): Unit = peer.Update()
  override def matches(element: Package, peer: IPackage) = element.id == peer.get_PackageID
}