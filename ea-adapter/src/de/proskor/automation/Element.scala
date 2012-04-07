package de.proskor.automation

trait Element {
  def id: Int
  var name: String
  var stereotype: String
  def parent: Option[Element]
  def pkg: Package
  def elements: Collection[Element]
  def connectors: Collection[Connector]
}