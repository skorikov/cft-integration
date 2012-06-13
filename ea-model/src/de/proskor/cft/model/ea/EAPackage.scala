package de.proskor.cft.model.ea

import de.proskor.cft.model.Container
import de.proskor.cft.model.Package
import de.proskor.cft.model.Component
import de.proskor.cft.model.Element
import de.proskor.cft.model.ea.peers.{ElementPeered, PackagePeer, PackagePeered, ProxyPeer, RepositoryPeer}

class EAPackage(var peer: PackagePeer) extends PackagePeered with Package {
  override def equals(that: Any): Boolean = that match {
    case pkg: EAPackage => pkg.peer.id == peer.id
    case _ => false
  }

  def name: String = peer.name
  def name_=(name: String) { peer.name = name }

  override def parent: Option[Package] =
    if (peer.isInstanceOf[ProxyPeer]) None
    else Some(peer.container.map(new EAPackage(_)).getOrElse(new EARepository(RepositoryPeer.instance)))
  override def elements: Set[Element] = components ++ packages
  override def components: Set[Component] = peer.elements.withFilter(_.stereotype == "Component").map(new EAComponent(_))
  override def packages: Set[Package] = peer.packages.map(new EAPackage(_))
  override def add(element: Element) = element match {
    case ea: PackagePeered => ea.peer = peer.add(ea.peer)
    case ea: ElementPeered => ea.peer = peer.add(ea.peer)
  }
  override def remove(element: Element) = element match {
    case ea: PackagePeered => ea.peer = peer.remove(ea.peer)
    case ea: ElementPeered => ea.peer = peer.remove(ea.peer)
  }
}