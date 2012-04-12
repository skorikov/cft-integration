package de.proskor.cft.model.ea

import de.proskor.cft.model.Container
import de.proskor.cft.model.Package
import de.proskor.cft.model.Component
import de.proskor.cft.model.Element
import de.proskor.cft.model.ea.peers.PackagePeer
import de.proskor.cft.model.ea.peers.RepositoryPeer

class EAPackage(var peer: PackagePeer) extends PackagePeered with Package {
  override def parent: Option[Container] =
    if (peer.isProxy) None
    else Some(peer.container.map(new EAPackage(_)).getOrElse(new EARepository(RepositoryPeer.instance)))
  override def elements: Set[Element] = components ++ packages
  override def components: Set[Component] = peer.elements.withFilter(_.stereotype == "Component").map(new EAComponent(_))
  override def packages: Set[Package] = peer.packages.map(new EAPackage(_))
  override def add(element: Element) {}
  override def remove(element: Element) {}
}