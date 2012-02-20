package de.proskor.cft.model

trait Inport extends Port

object Inport {
  def apply(name: String): Inport = CftFactory.default.createInport(name)
  def apply(parent: Component, name: String): Inport = CftFactory.default.createInport(parent, name)
}