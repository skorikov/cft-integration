package de.proskor.cft.model.ea

import de.proskor.cft.model.Element
import de.proskor.cft.model.Container
import de.proskor.cft.model.Package
import de.proskor.cft.model.Repository
import de.proskor.cft.model.Component
import de.proskor.cft.model.ea.peers.{PackagePeer, RepositoryPeered, PackagePeered, RepositoryPeer}
import de.proskor.cft.model.ea.peers.impl.EAProxyPeer

class EARepository(var peer: RepositoryPeer) extends RepositoryPeered with Repository {
  override def equals(that: Any): Boolean = that match {
    case repository: EARepository => true
    case _ => false
  }

  override def name: String = "/"
  override def name_=(name: String) {}

  override def parent: Option[Package] = None
  override def elements: Set[Element] = packages.asInstanceOf[Set[Element]]
  override def components: Set[Component] = Set()
  override def packages: Set[Package] = peer.packages.map(new EAPackage(_))
  override def add(element: Element) = element match {
    case ea: PackagePeered => ea.peer = peer.add(ea.peer)
  }
  override def remove(element: Element) = element match {
    case ea: PackagePeered => ea.peer = peer.remove(ea.peer)
  }
}