package de.proskor.cft.model.ea.peers

trait ContainerPeer extends Peer {
  def elements: Set[Peer]
  def elementsOfType(stereotypes: String*): Set[Peer]
  def addElement(name: String, stereotype: String): Peer
  def deleteElement(element: Peer): Unit
}