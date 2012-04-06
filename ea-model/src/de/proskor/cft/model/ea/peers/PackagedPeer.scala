package de.proskor.cft.model.ea.peers

trait PackagedPeer extends Peer {
  def packages: Set[PackagedPeer]
  def addPackage(name: String): PackagedPeer with ContainerPeer
  def deletePackage(pkg: PackagedPeer): Unit
}