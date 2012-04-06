package de.proskor.cft.model.ea.peers

class RepositoryPeer extends EAPeer with ConnectedPeer {
  val repository = de.proskor.automation.Repository
  def id: Int = 0

  def name: String = "/"
  def name_=(name: String) {}

  def stereotype: String = ""
  def stereotype_=(stereotype: String) {}

  // TODO
  override def parent: EAPeer = null

  override def elements: Set[Peer] =
    repository.models.map(new PackagePeer(_)).toSet

  override def elementsOfType(stereotypes: String*): Set[Peer] = Set()

  override def addElement(name: String, stereotype: String): Peer = null

  override def deleteElement(kid: Peer) {
    // TODO
  }

  override def packages: Set[PackagedPeer] = repository.models.map(new PackagePeer(_)).toSet
  override def deletePackage(pkg: PackagedPeer) {
    val index: Int = repository.models.indexOf(pkg.asInstanceOf[PackagePeer].pkg)
    if (index >= 0) repository.models.deleteAt(index)
  }
  override def addPackage(name: String): PackagedPeer with ContainerPeer = {
    val kid = repository.models.add(name, "Package")
    new PackagePeer(kid)
  }

  override def connectedElements: Set[Peer] = Set[Peer]()

  // TODO
  override def connect(kid: Peer) {}

  // TODO
  override def disconnect(kid: Peer) {}
}