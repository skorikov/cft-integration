package de.proskor.cft.model

trait Source extends Element {
  override def parent: Option[Component]
}