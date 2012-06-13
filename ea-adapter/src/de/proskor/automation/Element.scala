package de.proskor.automation

trait Element extends Identity with Named with Stereotyped with Container {
  def parent: Option[Element]
  def pkg: Package
  def connectors: Collection[Connector]
}