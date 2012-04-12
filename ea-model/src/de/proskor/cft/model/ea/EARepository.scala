package de.proskor.cft.model.ea

import de.proskor.cft.model.Element
import de.proskor.cft.model.Container
import de.proskor.cft.model.Package
import de.proskor.cft.model.Repository
import de.proskor.cft.model.Component
import de.proskor.cft.model.ea.peers.RepositoryPeer

class EARepository(var peer: RepositoryPeer) extends Repository {
  override def name: String = "/"
  override def name_=(name: String) {}

  override def parent: Option[Container] = None
  override def elements: Set[Element] = packages.asInstanceOf[Set[Element]]
  override def components: Set[Component] = Set()
  override def packages: Set[Package] = peer.packages.map(new EAPackage(_))
  override def add(element: Element) {}
  override def remove(element: Element) {}
}