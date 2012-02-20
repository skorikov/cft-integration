package de.proskor.cft.model.ea
import de.proskor.cft.model._
import de.proskor.cft.model.ea.peers._

private class EARepository(initialPeer: EARepositoryPeer) extends EAElement(initialPeer) with Repository {
  override def name = "/"
  override def name_=(name: String) {}
  override def parent: Option[Container] = None

  def packages: Set[Package] = peer.packages.map(EAFactory.create).asInstanceOf[Set[Package]]
  def components: Set[Component] = null
  def elements: Set[Element] = null

  def +=(element: Element) {
    require(element.isInstanceOf[EAPackage])
    val el = element.asInstanceOf[EAPackage]
    el.parent foreach {
      case container => container -= el
    }
    el.peer = peer.addPackage(element.name)
  }

  def -=(element: Element) {
    require(element.isInstanceOf[EAPackage])
    val pkg = element.asInstanceOf[EAPackage]
    pkg.peer match {
      case peer: EAProxyPeer =>
      case pkgPeer: EAPackagePeer => peer.deletePackage(pkgPeer); pkg.peer = new EAProxyPeer(pkgPeer)
    }
  }
}