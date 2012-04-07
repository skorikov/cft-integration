package de.proskor.automation

trait Diagram {
  def id: Int
  var name: String
  var stereotype: String
  def nodes: Collection[Node]
}