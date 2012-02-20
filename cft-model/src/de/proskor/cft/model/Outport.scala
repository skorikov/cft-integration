package de.proskor.cft.model

trait Outport extends Port

object Outport {
  def apply(name: String): Outport = CftFactory.default.createOutport(name)
  def apply(parent: Component, name: String): Outport = CftFactory.default.createOutport(parent, name)
}