package de.proskor.automation

trait Element extends Identity with Named with Stereotyped with Container {
  var author: String
  def parent: Option[Element]
  def pkg: Package
  def connectors: Collection[Connector]
  def taggedValues: Collection[TaggedValue]
}