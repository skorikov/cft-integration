package de.proskor.cft.model

trait And extends Gate

object And {
  def apply(name: String): And = CftFactory.default.createAnd(name)
  def apply(parent: Component, name: String): And = CftFactory.default.createAnd(parent, name)
  def unapply(and: And): Option[(String, Set[Source])] = Some(and.name, and.inputs)
}