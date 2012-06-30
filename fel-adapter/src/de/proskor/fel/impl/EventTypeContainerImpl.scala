package de.proskor.fel.impl

import java.util.{List => JavaList}

import scala.collection.JavaConversions.seqAsJavaList

import de.proskor.automation.Element
import de.proskor.automation.Repository
import de.proskor.fel.container.EventInstanceContainer
import de.proskor.fel.container.EventTypeContainer
import de.proskor.fel.event.EventType

class EventTypeContainerImpl(val peer: Element) extends EntityImpl(peer) with EventTypeContainer {
  override def getEvents: JavaList[EventType] = {
    val events = for {
      connector <- peer.connectors
      if connector.target == peer && connector.stereotype == "belongsTo"
      source = connector.source
    } yield new EventTypeImpl(source)
    events.toList
  }

  override def getInstances: JavaList[EventInstanceContainer] = {
    val instances = for {
      connector <- peer.connectors
      if connector.target == peer && connector.stereotype == "instanceOf"
      source = connector.source
    } yield new EventInstanceContainerImpl(source)
    instances.toList
  }

  override def createEvent(name: String): EventType = {
    val repository = Repository.instance
    val model = repository.models.headOption getOrElse repository.models.add("Model", "Package")
    val fel = model.packages.find(_.name == "FEL") getOrElse model.packages.add("FEL", "Package")
    val event = fel.elements.add(name, "Object")
    event.stereotype = "EventType"
    val connector = event.connectors.add("", "Connector"); connector.stereotype = "belongsTo"
    connector.source = event
    connector.target = peer
    new EventTypeImpl(event)
  }

  override def addInstance(instance: EventInstanceContainer) {}

  override def getChildren: JavaList[EventTypeContainer] = List() // TODO
  override def getParent: EventTypeContainer = null // TODO
}