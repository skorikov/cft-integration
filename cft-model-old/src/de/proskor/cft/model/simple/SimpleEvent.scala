package de.proskor.cft.model.simple

import de.proskor.cft.model.Event
import de.proskor.cft.model.Component

private class SimpleEvent(initialName: String) extends SimpleElement(initialName) with Event {
  override def parent: Option[Component] = container.asInstanceOf[Option[Component]]
}