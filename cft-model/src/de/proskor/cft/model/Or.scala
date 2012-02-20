package de.proskor.cft.model

trait Or extends Gate

object Or {
  def apply(name: String): Or = CftFactory.default.createOr(name)
  def apply(parent: Component, name: String): Or = CftFactory.default.createOr(parent, name)
  def unapply(or: Or): Option[(String, Set[Source])] = Some(or.name, or.inputs)
}