package de.proskor.automation

trait Node extends Identity with Rectangular {
  def diagram: Diagram
  var element: Element
  var sequence: Int
}