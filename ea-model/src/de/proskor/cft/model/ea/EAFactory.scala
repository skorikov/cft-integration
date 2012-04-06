package de.proskor.cft.model.ea
import de.proskor.cft.model._
import de.proskor.cft.model.ea.peers._

object EAFactory extends Factory {
  override def createEvent(name: String): Event = new EAEvent(new EAProxyPeer(name, "Event"))
  override def createComponent(name: String): Component = new EAComponent(new EAProxyPeer(name, "Component"))
  override def createOutport(name: String): Outport = new EAOutport(new EAProxyPeer(name, "Output"))
  override def createInport(name: String): Inport = new EAInport(new EAProxyPeer(name, "Input"))
  override def createAnd(name: String): And = new EAAnd(new EAProxyPeer(name, "AND"))
  override def createOr(name: String): Or = new EAOr(new EAProxyPeer(name, "OR"))
  override def createPackage(name: String): Package = new EAPackage(new EAProxyPeer(name, ""))
  override def createRepository(name: String): Repository = new EARepository(new RepositoryPeer)

  private[ea] def create(peer: Peer): Element = peer match {
    case elementPeer: ElementPeer => createElement(elementPeer)
    case repositoryPeer: RepositoryPeer => createRepository(repositoryPeer)
    case peer: EAProxyPeer => throw new IllegalStateException
    case pkgPeer: PackagedPeer with ContainerPeer => createPackage(pkgPeer)
  }

  private[ea] def createPackage(peer: PackagedPeer with ContainerPeer): EAPackage = new EAPackage(peer)

  private[ea] def createElement(peer: ElementPeer): EAElement = peer.stereotype match {
    case "Event" => new EAEvent(peer)
    case "AND" => new EAAnd(peer.asInstanceOf[ConnectedPeer])
    case "OR" => new EAOr(peer.asInstanceOf[ConnectedPeer])
    case "Input" => new EAInport(peer.asInstanceOf[ConnectedPeer])
    case "Output" => new EAOutport(peer.asInstanceOf[ConnectedPeer])
    case "Component" => new EAComponent(peer.asInstanceOf[ContainerPeer])
  }

  private[ea] def createRepository(peer: RepositoryPeer): EARepository = new EARepository(peer)
}