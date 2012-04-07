package de.proskor.automation

trait Connector {
  def id: Int
  var name: String
  var stereotype: String
  def source: Element
  def target: Element
}