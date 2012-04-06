package de.proskor.cft.model.ea
import de.proskor.cft.model._
import de.proskor.cft.model.ea.peers._

private class EAPackage(var peer: PackagedPeer with ContainerPeer) extends EAElementTrait[PackagedPeer with ContainerPeer] with Package {
  def components: Set[Component] = peer.elementsOfType("Component").map(EAFactory.create).asInstanceOf[Set[Component]]
  def packages: Set[Package] = peer.packages.map(EAFactory.create).asInstanceOf[Set[Package]]
  def elements: Set[Element] = peer.elements.map(EAFactory.create)

  def add(element: Element) {
    require(element.isInstanceOf[EAPackage] || element.isInstanceOf[EAComponent])
    val el = element.asInstanceOf[EAElement]
    el.parent foreach {
      case container => container -= el
    }
    el.peer = element match {
      case pkg: EAPackage => peer.addPackage(pkg.name)
      case component: EAComponent => peer.addElement(component.name, "Component")
    }
  }

  def remove(element: Element) {
    require(element.isInstanceOf[EAPackage] || element.isInstanceOf[EAComponent])
    val el = element.asInstanceOf[EAElement]
    el.peer match {
      case peer: EAProxyPeer =>
      case pkgPeer: PackagePeer => peer.deletePackage(pkgPeer); el.peer = new EAProxyPeer(pkgPeer)
      case elPeer: ElementPeer => peer.deleteElement(elPeer); el.peer = new EAProxyPeer(elPeer)
    }
  }
}