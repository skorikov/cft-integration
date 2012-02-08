package de.proskor.model

trait Diagram extends Named with Identity {
  val nodes: List[Node]
  def addNode: Node
}