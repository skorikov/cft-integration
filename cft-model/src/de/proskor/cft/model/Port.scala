package de.proskor.cft.model

trait Port extends Source {
  def input: Option[Source]
  def add(input: Source): Unit
  def remove(input: Source): Unit
  final def +=(input: Source): Unit = add(input)
  final def -=(input: Source): Unit = remove(input)
}

object Port {
  def unapply(port: Port): Option[(String, Option[Source])] = Some(port.name, port.input)
}