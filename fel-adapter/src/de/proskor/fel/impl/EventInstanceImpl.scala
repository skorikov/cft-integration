package de.proskor.fel.impl

import de.proskor.automation.Element
import de.proskor.fel.event.EventInstance
import de.proskor.fel.event.EventType
import de.proskor.fel.container.EventInstanceContainer

class EventInstanceImpl(peer: Element) extends EntityImpl(peer) with EventInstance {
  override def getContainer: EventInstanceContainer = {
    val types = for {
      connector <- peer.connectors
      if connector.source == peer && connector.stereotype == "belongsTo"
      target = connector.target
    } yield new EventInstanceContainerImpl(target)
    types.headOption.orNull
  }

  override def getType: EventType = {
    val types = for {
      connector <- peer.connectors
      if connector.source == peer && connector.stereotype == "instanceOf"
      target = connector.target
    } yield new EventTypeImpl(target)
    types.headOption.orNull
  }
}