package de.proskor.fel.impl

import de.proskor.automation.Element
import de.proskor.fel.Entity;

class EntityImpl(peer: Element) extends Entity {
  override def getId: Int = peer.id
  override def getGuid: String = peer.guid
  override def getName: String = peer.name
  override def getAuthor: String = "andrey"
  override def setAuthor(author: String) {}
  override def getDescription: String = "desc"
  override def setDescription(description: String) {}
}