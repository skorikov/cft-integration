package de.proskor.model.cft

trait Output extends Port with Source {
  override def toString = "output " + name + { source match {
    case Some(s: Source) => " {\n  ref " + s.elementName + ";\n}"
    case _ => ";"
  }}
  def matches(that: Output) = this.elementName == that.elementName
}