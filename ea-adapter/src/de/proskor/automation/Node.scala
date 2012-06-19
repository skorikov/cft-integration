package de.proskor.automation

trait Node extends Rectangular {
  def diagram: Diagram
  var element: Element
  var sequence: Int
}