package de.proskor.cft.model

trait Gate extends Source {
  def inputs: Set[Source]
  def +=(input: Source): Unit
  def -=(input: Source): Unit
}