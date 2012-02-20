package de.proskor.cft.model

trait Port extends Source {
  def input: Option[Source]
  def +=(input: Source): Unit
  def -=(input: Source): Unit
}