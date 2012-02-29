package de.proskor.cft.model

trait Event extends Source

object Event {
  def apply(name: String): Event = Factory.createEvent(name)
  def apply(parent: Component, name: String): Event = Factory.createEvent(parent, name)
  def unapply(event: Event): Option[String] = Some(event.name)
}