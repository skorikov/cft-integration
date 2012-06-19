package de.proskor.fel.impl

import java.util.{List => JavaList}
import collection.JavaConversions._
import de.proskor.automation.Element
import de.proskor.automation.Package
import de.proskor.fel.container.EventInstanceContainer
import de.proskor.fel.event.EventInstance
import de.proskor.fel.container.EventTypeContainer
import de.proskor.automation.Repository

class EventInstanceContainerImpl(peer: Element) extends EntityImpl(peer) with EventInstanceContainer {
  override def getType: EventTypeContainer = {
    val types = for {
      connector <- peer.connectors
      if connector.source == peer && connector.stereotype == "instanceOf"
      target = connector.target
    } yield new EventTypeContainerImpl(target)
    types.headOption.orNull
  }

  override def getEvents: JavaList[EventInstance] = {
    val instances = for {
      connector <- peer.connectors
      if connector.target == peer && connector.stereotype == "instanceOf"
      source = connector.source
    } yield new EventInstanceImpl(source)
    instances.toList
  }

  override def createEvent(name: String): EventInstance = null
}