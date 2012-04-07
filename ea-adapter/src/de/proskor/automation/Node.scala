package de.proskor.automation

trait Node {
  def id: Int
  var left: Int
  var top: Int
  var width: Int
  var height: Int
  def diagram: Diagram
  def element: Element
}