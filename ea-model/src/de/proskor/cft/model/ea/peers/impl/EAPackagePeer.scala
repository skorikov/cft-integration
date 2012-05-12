package de.proskor.cft.model.ea.peers.impl

import de.proskor.automation.Package
import de.proskor.cft.model.ea.peers._

class EAPackagePeer(val peer: Package) extends PackagePeer {
  override def id: Int = peer.id

  override def name: String = peer.name
  override def name_=(name: String) { peer.name = name }

  override def stereotype: String = null
  override def stereotype_=(stereotype: String) {}

  override val isProxy: Boolean = false

//  override def parent: Option[PackagePeer] = peer.parent.map(new EAPackagePeer(_))
//  override def pkg: Option[PackagePeer] = Some(new EAPackagePeer(peer.pkg)) // TODO

  override def elements: Set[ElementPeer] = peer.elements.map(new EAElementPeer(_)).toSet
  override def packages: Set[PackagePeer] = peer.packages.map(new EAPackagePeer(_)).toSet
  override def container: Option[PackagePeer] = peer.parent.map(new EAPackagePeer(_))
}