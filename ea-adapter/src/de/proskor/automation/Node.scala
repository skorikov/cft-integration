package de.proskor.automation

trait Node extends Identity with Rectangular {
  def diagram: Diagram
  def element: Element
}