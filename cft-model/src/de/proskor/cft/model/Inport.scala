package de.proskor.cft.model

trait Inport extends Port

object Inport {
  def apply(name: String): Inport = Factory.createInport(name)
  def apply(parent: Component, name: String): Inport = Factory.createInport(parent, name)
}