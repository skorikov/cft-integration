package de.proskor.fel.impl

import java.util.{List => JavaList}

import scala.collection.JavaConversions.seqAsJavaList

import de.proskor.automation.Element
import de.proskor.automation.Repository
import de.proskor.fel.container.EventInstanceContainer
import de.proskor.fel.container.EventTypeContainer
import de.proskor.fel.event.EventInstance

class EventInstanceContainerImpl(val peer: Element) extends EntityImpl(peer) with EventInstanceContainer {
  override def getType: EventTypeContainer = {
    val types = for {
      connector <- peer.connectors
      if connector.source == peer && connector.stereotype == "instanceOf"
      target = connector.target
    } yield new EventTypeContainerImpl(target)
    types.headOption.orNull
  }

  override def getEvents: JavaList[EventInstance] = {
    val events = for {
      element <- peer.elements if element.stereotype == "Event"
    } yield new EventInstanceImpl(element)
    events.toList
  }

  override def getChildren: JavaList[EventInstanceContainer] = List() // TODO
  override def getParent: EventInstanceContainer = null // TODO
}