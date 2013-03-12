package de.proskor.cft.model

trait Target extends Source {
  def inputs: Set[Source]
  def add(input: Source): Unit
  def remove(input: Source): Unit
  final def +=(input: Source): Unit = add(input) 
  final def -=(input: Source): Unit = remove(input)
}

object Target {
  def unapply(target: Target): Option[(String, Set[Source])] = Some(target.name, target.inputs)
}