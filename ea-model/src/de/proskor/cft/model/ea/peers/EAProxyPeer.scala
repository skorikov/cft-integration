package de.proskor.cft.model.ea.peers
import de.proskor.cft.model.ea._

class EAProxyPeer(var name: String, var stereotype: String) extends EAPeer {
  def this(peer: Peer) = this(peer.name, peer.stereotype)

  override def id: Int =
    throw new IllegalStateException

  override def elements: Set[Peer] =
    throw new IllegalStateException

  override def elementsOfType(stereotypes: String*): Set[Peer] =
    throw new IllegalStateException

  override def packages: Set[PackagedPeer] =
    throw new IllegalStateException

  def createPeer(name: String, stereotype: String): EAPeer =
    throw new IllegalStateException

  override def parent: EAPeer =
    throw new IllegalStateException

  override def deletePackage(pkg: PackagedPeer) =
    throw new IllegalStateException

  override def deleteElement(el: Peer) =
    throw new IllegalStateException

  override def addPackage(name: String): EAPeer =
    throw new IllegalStateException

  override def addElement(name: String, stereotype: String): EAPeer =
    throw new IllegalStateException

  override def connectedElements: Set[Peer] =
    throw new IllegalStateException

  override def connect(element: Peer) =
    throw new IllegalStateException

  override def disconnect(element: Peer) =
    throw new IllegalStateException
}