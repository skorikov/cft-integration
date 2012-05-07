package de.proskor.cft.model.ea.peers

import de.proskor.automation.Element

class EAElementPeer(val peer: Element) extends ElementPeer {
  override def id: Int = peer.id

  override def name: String = peer.name
  override def name_=(name: String) { peer.name = name }

  override def stereotype: String = peer.stereotype
  override def stereotype_=(stereotype: String) { peer.stereotype = stereotype }

  override val isProxy: Boolean = false

  override def parent: Option[ElementPeer] = peer.parent.map(new EAElementPeer(_))
  override def pkg: Option[PackagePeer] = Some(new EAPackagePeer(peer.pkg)) // TODO

  override def elements: Set[ElementPeer] = peer.elements.map(new EAElementPeer(_)).toSet
  override def inputs: Set[ElementPeer] = Set()
  override def connect(element: ElementPeer) {}
  override def disconnect(element: ElementPeer) {}

  override def add(element: ElementPeer): ElementPeer = if (element.isProxy) {
    val result = peer.elements.add(element.name, "Object")
    result.stereotype = element.stereotype
    new EAElementPeer(result)
  } else if (!elements.contains(element)) {
    element.parent.get.remove(element)
    val result = peer.elements.add(element.name, "Object")
    result.stereotype = element.stereotype
    new EAElementPeer(result)
  } else element

  override def remove(element: ElementPeer): ElementPeer = if (element.isProxy) {
    element
  } else {
    element
  }
}