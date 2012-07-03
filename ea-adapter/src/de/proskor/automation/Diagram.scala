package de.proskor.automation

trait Diagram extends Identity with Named with Stereotyped {
  def nodes: Collection[Node]
  def pkg: Package
  def open(): Unit
}