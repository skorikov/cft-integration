package de.proskor.cft.model

trait Gate extends Source {
  def inputs: Set[Source]
  def add(input: Source): Unit
  def remove(input: Source): Unit
  final def +=(input: Source): Unit = add(input) 
  final def -=(input: Source): Unit = remove(input)
}

object Gate {
  def unapply(gate: Gate): Option[(String, Set[Source])] = Some(gate.name, gate.inputs)
}