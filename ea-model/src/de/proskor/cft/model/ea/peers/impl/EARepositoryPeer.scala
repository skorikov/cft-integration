package de.proskor.cft.model.ea.peers.impl

import de.proskor.automation.Repository
import de.proskor.cft.model.ea.peers._

class EARepositoryPeer(val peer: Repository) extends RepositoryPeer {
  override val id: Int = 0

  override def name: String = "/"
  override def name_=(name: String) {}

  override def stereotype: String = ""
  override def stereotype_=(stereotype: String) {}

  override val isProxy: Boolean = false

  override def packages: Set[PackagePeer] = peer.models.map(new EAPackagePeer(_)).toSet

  override def add(element: PackagePeer): PackagePeer = if (element.isProxy) {
    val result = peer.models.add(element.name, "Package")
    new EAPackagePeer(result)
  } else element

  override def remove(element: PackagePeer): PackagePeer = if (element.isProxy) {    
    element
  } else {
    peer.models.delete(element.asInstanceOf[EAPackagePeer].peer)
    new EAProxyPeer(element.name, element.stereotype)
  }
}