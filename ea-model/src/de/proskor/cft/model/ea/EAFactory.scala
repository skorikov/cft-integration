package de.proskor.cft.model.ea

import de.proskor.cft.model._
import de.proskor.cft.model.ea.peers._
import de.proskor.cft.model.ea.peers.impl._

object EAFactory extends Factory {
  override def createRepository(name: String): Repository = new EARepository(RepositoryPeer.instance)
  override def createPackage(name: String): Package = new EAPackage(new EAProxyPeer(name, null))
  override def createComponent(name: String): Component = new EAComponent(new EAProxyPeer(name, "Component"))
  override def createEvent(name: String): Event = new EAEvent(new EAProxyPeer(name, "Event"))
  override def createAnd(name: String): And = new EAAnd(new EAProxyPeer(name, "AND"))
  override def createOr(name: String): Or = new EAOr(new EAProxyPeer(name, "OR"))
  override def createInport(name: String): Inport = new EAInport(new EAProxyPeer(name, "Input"))
  override def createOutport(name: String): Outport =  new EAOutport(new EAProxyPeer(name, "Output"))
}