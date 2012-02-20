package de.proskor.cft.model.ea.peers
import de.proskor.cft.model.ea._

class EARepositoryPeer(peer: cli.EA.IRepository) extends EAPeer {
  val id: Int = 0

  def name = "/"
  def name_=(name: String) {}

  def stereotype: String = ""
  def stereotype_=(stereotype: String) {}

  def createPeer(name: String, stereotype: String): EAElementPeer = null

  private def kids: Set[cli.EA.IPackage] = {
    val collection = peer.get_Models.asInstanceOf[cli.EA.ICollection]
    val peers = for (i <- 0 until collection.get_Count) yield collection.GetAt(i.toShort).asInstanceOf[cli.EA.IPackage]
    peers.toSet
  }
  def elements: Set[EAPeer] =
    for (kid <- kids) yield new EAPackagePeer(kid)

  def elementsOfType(stereotypes: String*): Set[EAPeer] = Set()

  def packages: Set[EAPeer] = {
    val collection = peer.get_Models.asInstanceOf[cli.EA.ICollection]
    val peers = for (i <- 0 until collection.get_Count; pkg = collection.GetAt(i.toShort).asInstanceOf[cli.EA.IPackage])
      yield new EAPackagePeer(pkg)
    peers.toSet
  }

  def parent: EAPeer = this

  def addElement(name: String, stereotype: String): EAPeer = null

  def addPackage(name: String): EAPackagePeer = {
    val collection = peer.get_Models.asInstanceOf[cli.EA.ICollection]
    val pkgPeer = collection.AddNew(name, "Package").asInstanceOf[cli.EA.IPackage]
    pkgPeer.Update()
    collection.Refresh()
    EAFactory.cache += pkgPeer.get_PackageID -> pkgPeer
    new EAPackagePeer(pkgPeer)
  }

  def deletePackage(pkg: EAPeer) {
    val collection = peer.get_Models.asInstanceOf[cli.EA.ICollection]
    var i = 0
    var found = false
    while (i < collection.get_Count && !found) {
      if (collection.GetAt(i.toShort).asInstanceOf[cli.EA.IPackage].get_PackageID == pkg.asInstanceOf[EAPackagePeer].peer.get_PackageID)
        found = true
      else
        i += 1
    }
    if (found) {
      collection.Delete(i.toShort)
      collection.Refresh()
      EAFactory.cache -= pkg.id
      peer.RefreshModelView(0)
    }
  }

  def deleteElement(el: EAPeer) {}

  def connectedElements: Set[EAPeer] = Set[EAPeer]()
  def connect(element: EAPeer) {}
  def disconnect(element: EAPeer) {}
}