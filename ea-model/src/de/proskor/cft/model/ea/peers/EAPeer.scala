package de.proskor.cft.model.ea.peers
import de.proskor.cft.model.ea._

abstract class EAPeer {
  var name: String
  var stereotype: String

  def id: Int
  def parent: EAPeer

  def elements: Set[EAPeer]
  def elementsOfType(stereotypes: String*): Set[EAPeer]
  def addElement(name: String, stereotype: String): EAPeer
  def deleteElement(element: EAPeer): Unit

  def packages: Set[EAPeer]
  def addPackage(name: String): EAPeer
  def deletePackage(pkg: EAPeer): Unit

  def connectedElements: Set[EAPeer]
  def connect(element: EAPeer): Unit
  def disconnect(element: EAPeer): Unit
}