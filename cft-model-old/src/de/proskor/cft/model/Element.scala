package de.proskor.cft.model

trait Element {
  var name: String
  def parent: Option[Container]
}