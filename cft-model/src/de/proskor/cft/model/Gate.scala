package de.proskor.cft.model

trait Gate extends Target

object Gate {
  def unapply(gate: Gate): Option[(String, Set[Source])] = Some(gate.name, gate.inputs)
}