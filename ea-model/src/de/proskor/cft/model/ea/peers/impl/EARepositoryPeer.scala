package de.proskor.cft.model.ea.peers.impl

import de.proskor.automation.Repository
import de.proskor.cft.model.ea.peers._

class EARepositoryPeer(val peer: Repository) extends RepositoryPeer {
  override def packages: Set[PackagePeer] = peer.models.map(new EAPackagePeer(_)).toSet

  override def add(element: PackagePeer): PackagePeer = if (element.isInstanceOf[ProxyPeer]) {
    val result = peer.models.add(element.name, "Package")
    new EAPackagePeer(result)
  } else element

  override def remove(element: PackagePeer): PackagePeer = if (element.isInstanceOf[ProxyPeer]) {    
    element
  } else {
    peer.models.delete(element.asInstanceOf[EAPackagePeer].peer)
    new EAProxyPeer(element.name, "")
  }
}