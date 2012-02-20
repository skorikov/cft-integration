package de.proskor.cft.model.ea.peers
import de.proskor.cft.model.ea._

class EAProxyPeer(var name: String, var stereotype: String) extends EAPeer {
  def this(peer: EAPeer) = this(peer.name, peer.stereotype)

  def id: Int =
    throw new IllegalStateException

  def elements: Set[EAPeer] =
    throw new IllegalStateException

  def elementsOfType(stereotypes: String*): Set[EAPeer] =
    throw new IllegalStateException

  def packages: Set[EAPeer] =
    throw new IllegalStateException

  def createPeer(name: String, stereotype: String): EAPeer =
    throw new IllegalStateException

  def parent: EAPeer =
    throw new IllegalStateException

  def deletePackage(pkg: EAPeer) =
    throw new IllegalStateException

  def deleteElement(el: EAPeer) =
    throw new IllegalStateException

  def addPackage(name: String): EAPeer =
    throw new IllegalStateException

  def addElement(name: String, stereotype: String): EAPeer =
    throw new IllegalStateException

  def connectedElements: Set[EAPeer] =
    throw new IllegalStateException

  def connect(element: EAPeer) =
    throw new IllegalStateException

  def disconnect(element: EAPeer) =
    throw new IllegalStateException
}