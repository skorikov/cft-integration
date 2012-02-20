package de.proskor.cft.model.ea
import de.proskor.cft.model._
import de.proskor.cft.model.ea.peers._

object EAFactory extends CftFactory {
  // always set this first
  var repositoryPeer: cli.EA.IRepository = null
  private var repository: Option[EARepository] = None
  private[ea] var cache: Map[Int, Any] = Map[Int, Any]()

  def createEvent(name: String): Event = new EAEvent(new EAProxyPeer(name, "Event"))
  def createComponent(name: String): Component = new EAComponent(new EAProxyPeer(name, "Component"))
  def createOutport(name: String): Outport = new EAOutport(new EAProxyPeer(name, "Output"))
  def createInport(name: String): Inport = new EAInport(new EAProxyPeer(name, "Input"))
  def createAnd(name: String): And = new EAAnd(new EAProxyPeer(name, "And"))
  def createOr(name: String): Or = new EAOr(new EAProxyPeer(name, "Or"))
  def createPackage(name: String): Package = new EAPackage(new EAProxyPeer(name, ""))
  def createRepository(name: String): Repository = repository match {
    case Some(repository) => repository
    case None => if (repositoryPeer != null) {
      val rep = createRepository(new EARepositoryPeer(repositoryPeer))
      repository = Some(rep)
      rep
    } else throw new IllegalStateException
  }

  private[ea] def element(id: Int): cli.EA.IElement = cache.getOrElse(id, repositoryPeer.GetElementByID(id)).asInstanceOf[cli.EA.IElement]
  private[ea] def pkg(id: Int): cli.EA.IPackage = cache.getOrElse(id, repositoryPeer.GetPackageByID(id)).asInstanceOf[cli.EA.IPackage]

  private[ea] def create(peer: EAPeer): EAElement = peer match {
    case peer: EAProxyPeer => throw new IllegalStateException
    case pkgPeer: EAPackagePeer => createPackage(pkgPeer)
    case elementPeer: EAElementPeer => createElement(elementPeer)
    case repositoryPeer: EARepositoryPeer => createRepository(repositoryPeer)
  }

  private[ea] def createPackage(peer: EAPackagePeer): EAPackage = new EAPackage(peer)

  private[ea] def createElement(peer: EAElementPeer): EAElement = peer.stereotype match {
    case "Event" => new EAEvent(peer)
    case "And" => new EAAnd(peer)
    case "Or" => new EAOr(peer)
    case "Input" => new EAInport(peer)
    case "Output" => new EAOutport(peer)
    case "Component" => new EAComponent(peer)
  }

  private[ea] def createRepository(peer: EARepositoryPeer): EARepository = new EARepository(peer)
}