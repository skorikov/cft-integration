package de.proskor.cft.model.ea
import de.proskor.cft.model._
import de.proskor.cft.model.ea.peers._

private class EAElement(var peer: Peer) extends EAElementTrait[Peer] with Element {
  override def equals(that: Any): Boolean = that match {
    case element: EAElement => id == element.id
    case _ => false
  }

  override def hashCode: Int = id
}