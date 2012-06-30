package de.proskor.fel.impl

import de.proskor.automation.Element
import de.proskor.fel.Entity;

class EntityImpl(peer: Element) extends Entity {
  override def getId: Int = peer.id
  override def getGuid: String = peer.guid
  override def getName: String = peer.name

  override def getAuthor: String = peer.author
  override def setAuthor(author: String) {
    peer.author = author
  }

  override def getDescription: String = peer.description
  override def setDescription(description: String) {
    peer.description = description
  }

  override def getQualifiedName: String = "TODO"
}