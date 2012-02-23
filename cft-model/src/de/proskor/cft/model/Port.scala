package de.proskor.cft.model

trait Port extends Source {
  def input: Option[Source]
  def +=(input: Source): Unit
  def -=(input: Source): Unit
}

object Port {
  def unapply(port: Port): Option[(String, Option[Source])] = Some(port.name, port.input)
}