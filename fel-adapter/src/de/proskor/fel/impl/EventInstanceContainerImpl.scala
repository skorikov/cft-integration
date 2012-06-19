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
      if connector.target == peer && connector.stereotype == "belongsTo"
      source = connector.source
    } yield new EventInstanceImpl(source)
    instances.toList
  }

  override def createEvent(name: String): EventInstance = {
    val repository = Repository.instance
    val pkg = repository.models.find(_.name == "Trash") getOrElse(repository.models.add("Trash", "Package"))
    val event = pkg.elements.add(name, "Object")
    event.stereotype = "Event"
    val connector = event.connectors.add("", "Connector"); connector.stereotype = "belongsTo"
    connector.source = event
    connector.target = peer
    new EventInstanceImpl(event)
  }
}