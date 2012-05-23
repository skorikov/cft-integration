package de.proskor.cft.model.ea.peers.impl

import de.proskor.automation.Package
import de.proskor.cft.model.ea.peers._

class EAPackagePeer(val peer: Package) extends PackagePeer {
  override def id: Int = peer.id

  override def name: String = peer.name
  override def name_=(name: String) { peer.name = name }

//  override def parent: Option[PackagePeer] = peer.parent.map(new EAPackagePeer(_))
//  override def pkg: Option[PackagePeer] = Some(new EAPackagePeer(peer.pkg)) // TODO

  override def elements: Set[ElementPeer] = peer.elements.map(new EAElementPeer(_)).toSet
  override def packages: Set[PackagePeer] = peer.packages.map(new EAPackagePeer(_)).toSet
  override def container: Option[PackagePeer] = peer.parent.map(new EAPackagePeer(_))

  override def add(element: ElementPeer): ElementPeer = if (element.isInstanceOf[ProxyPeer]) {
    val result = peer.elements.add(element.name, "Object")
    result.stereotype = element.stereotype
    new EAElementPeer(result)
  } else element

  override def remove(element: ElementPeer): ElementPeer = if (element.isInstanceOf[ProxyPeer]) {    
    element
  } else {
    peer.elements.remove(element.asInstanceOf[EAElementPeer].peer)
    new EAProxyPeer(element.name, element.stereotype)
  }

  override def add(element: PackagePeer): PackagePeer = if (element.isInstanceOf[ProxyPeer]) {
    val result = peer.packages.add(element.name, "Package")
    new EAPackagePeer(result)
  } else element

  override def remove(element: PackagePeer): PackagePeer = if (element.isInstanceOf[ProxyPeer]) {    
    element
  } else {
    peer.packages.remove(element.asInstanceOf[EAPackagePeer].peer)
    new EAProxyPeer(element.name, "")
  }
}