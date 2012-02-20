package de.proskor.cft.model

trait Event extends Source

object Event {
  def apply(name: String): Event = CftFactory.default.createEvent(name)
  def apply(parent: Component, name: String): Event = CftFactory.default.createEvent(parent, name)
  def unapply(event: Event): Option[String] = Some(event.name)
}