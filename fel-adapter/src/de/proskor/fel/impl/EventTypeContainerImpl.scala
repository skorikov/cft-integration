package de.proskor.fel.impl

import java.util.{List => JavaList}
import collection.JavaConversions._
import de.proskor.automation.Element
import de.proskor.automation.Package
import de.proskor.fel.container.EventTypeContainer
import de.proskor.fel.event.EventType
import de.proskor.fel.container.EventInstanceContainer
import de.proskor.automation.Repository

class EventTypeContainerImpl(peer: Element) extends EntityImpl(peer) with EventTypeContainer {
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

  override def createEvent(name: String): EventType = null
  override def addInstance(instance: EventInstanceContainer) {}
}