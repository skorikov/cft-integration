package de.proskor.fel.impl.ea

import java.util.{List => JavaList}
import collection.JavaConversions._
import de.proskor.fel.EventType
import de.proskor.automation.Element
import de.proskor.fel.EventInstance
import de.proskor.automation.Repository

class EventTypeImpl(peer: Element) extends EventType {
  override def getId: Int = peer.id
  override def getGuid: String = "TODO"
  override def getName: String = peer.name
  override def getAuthor: String = "TODO"
  override def getDescription: String = "TODO"

  override def getInstances: JavaList[EventInstance] = (for {
      connector <- peer.connectors
      if connector.target == this && connector.stereotype == "instanceOf"
      instance = connector.source
    } yield new EventInstanceImpl(instance)).toList
  
  override def addInstance(instance: EventInstance) {
  //  val models = Repository.instance.models
  //  val fel = models.find(_.name == "FEL") getOrElse models.add("FEL", "Package")
    val connector = peer.connectors.add("instanceOf", "instanceOf")
    connector.source = instance.asInstanceOf[EventInstanceImpl].peer
    connector.target = this.peer
  }
}