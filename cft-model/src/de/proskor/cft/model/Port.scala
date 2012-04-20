package de.proskor.cft.model

trait Port extends Target

object Port {
  def unapply(port: Port): Option[(String, Set[Source])] = Some(port.name, port.inputs)
}