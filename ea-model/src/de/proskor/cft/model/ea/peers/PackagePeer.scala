package de.proskor.cft.model.ea.peers

import de.proskor.automation.Package

class PackagePeer(val pkg: Package) extends PackagedPeer with ContainerPeer {
  def id: Int = 0

  def name: String = "/"
  def name_=(name: String) {}

  def stereotype: String = ""
  def stereotype_=(stereotype: String) {}

  def parent: ContainerPeer = pkg.parent match {
    case Some(pkg) => new PackagePeer(pkg)
    case None => new RepositoryPeer
  }

  def elements: Set[Peer] =
    pkg.packages.map(new PackagePeer(_)).toSet

  def elementsOfType(stereotypes: String*): Set[Peer] = Set()

  def addElement(name: String, stereotype: String): EAPeer = null

  def deleteElement(kid: Peer) {
    // TODO
  }

  def packages: Set[PackagedPeer] = Set()
  def deletePackage(pkg: PackagedPeer) {}
  def addPackage(name: String): PackagedPeer with ContainerPeer = null

  def connectedElements: Set[Peer] = Set[Peer]()

  // TODO
  def connect(kid: EAPeer) {}

  // TODO
  def disconnect(kid: EAPeer) {}
}