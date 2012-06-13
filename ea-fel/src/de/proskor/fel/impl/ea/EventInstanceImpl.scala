package de.proskor.fel.impl.ea

import java.util.{List => JavaList}
import de.proskor.automation.Element
import de.proskor.fel.EventInstance
import de.proskor.fel.EventInstanceContainer
import de.proskor.fel.EventType

class EventInstanceImpl(val peer: Element) extends EventInstance {
  override def getId: Int = peer.id
  override def getGuid: String = "TODO"
  override def getAuthor: String = "TODO"
  override def getDescription: String = "TODO"
  override def getEvent: EventType = null
  override def getContainer: EventInstanceContainer = null
}