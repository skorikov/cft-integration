package de.proskor.cft.model.ea.peers.impl

import de.proskor.automation.Element
import de.proskor.cft.model.ea.peers._

class EAElementPeer(val peer: Element) extends ElementPeer {
  override def id: Int = peer.id

  override def name: String = peer.name
  override def name_=(name: String) { peer.name = name }

  override def stereotype: String = peer.stereotype
  override def stereotype_=(stereotype: String) { peer.stereotype = stereotype }

  override def parent: Option[ElementPeer] = peer.parent.map(new EAElementPeer(_))
  override def pkg: Option[PackagePeer] = Some(new EAPackagePeer(peer.pkg))

  override def elements: Set[ElementPeer] = peer.elements.map(new EAElementPeer(_)).toSet
  override def inputs: Set[ElementPeer] = (for {
    connector <- peer.connectors if connector.target == peer
    source = connector.source
  } yield new EAElementPeer(source)).toSet

  override def connect(element: ElementPeer) {
    val connector = peer.connectors.add("", "Connector")
    connector.source = element.asInstanceOf[EAElementPeer].peer
    connector.target = this.peer
  }
  override def disconnect(element: ElementPeer) {
    peer.connectors.find(_.target == this.peer).foreach(peer.connectors.remove)
  }

  override def add(element: ElementPeer): ElementPeer = if (element.isInstanceOf[ProxyPeer]) {
    val result = peer.elements.add(element.name, "Object")
    result.stereotype = element.stereotype
    new EAElementPeer(result)
  } else if (!elements.contains(element)) {
    element.parent.get.remove(element)
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
}