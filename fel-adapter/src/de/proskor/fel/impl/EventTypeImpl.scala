package de.proskor.fel.impl

import java.util.{List => JavaList}

import scala.collection.JavaConversions.seqAsJavaList

import de.proskor.automation.Element
import de.proskor.fel.container.EventTypeContainer
import de.proskor.fel.event.EventInstance
import de.proskor.fel.event.EventType

class EventTypeImpl(private[impl] val peer: Element) extends EntityImpl(peer) with EventType {
  override def getContainer: EventTypeContainer = {
    val types = for {
      connector <- peer.connectors
      if connector.source == peer && connector.stereotype == "belongsTo"
      target = connector.target
    } yield new EventTypeContainerImpl(target)
    types.headOption.orNull
  }

  override def getInstances: JavaList[EventInstance] = {
    val instances = for {
      connector <- peer.connectors
      if connector.target == peer && connector.stereotype == "instanceOf"
      source = connector.source
    } yield new EventInstanceImpl(source)
    instances.toList
  }

  override def addInstance(instance: EventInstance) {}
}