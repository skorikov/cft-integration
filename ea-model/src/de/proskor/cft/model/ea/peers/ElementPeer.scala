package de.proskor.cft.model.ea.peers

import de.proskor.automation.Element

class ElementPeer(element: Element) extends Peer with ContainerPeer with PackagedPeer with ConnectedPeer {
  def id: Int = element.id

  def name: String = element.name
  def name_=(name: String) {
    element.name = name
  }

  def stereotype: String = element.stereotype
  def stereotype_=(stereotype: String) {
    element.stereotype = stereotype
  }

  // TODO
  override def parent: EAPeer = null

  override def elements: Set[Peer] =
    element.elements.map(new ElementPeer(_)).toSet

  override def elementsOfType(stereotypes: String*): Set[Peer] =
    element.elements.filter(kid => stereotypes.contains(kid.stereotype)).map(new ElementPeer(_)).toSet

  override def addElement(name: String, stereotype: String): Peer = {
    val typ = if (Set("Input", "Output").contains(stereotype)) "Port" else "Object"
    val result = element.elements.add(name, typ)
    result.stereotype = stereotype
    new ElementPeer(result)
  }

  override def deleteElement(kid: Peer) {
    // TODO
  }

  override def packages: Set[PackagedPeer] = Set()
  override def deletePackage(pkg: PackagedPeer) {}
  override def addPackage(name: String): EAPeer = null

  override def connectedElements: Set[Peer] = {
    val sources = for (connector <- element.connectors; if connector.target.id == this.id)
      yield new ElementPeer(connector.source)
    sources.toSet
  }

  // TODO
  override def connect(kid: Peer) {}

  // TODO
  override def disconnect(kid: Peer) {}
}