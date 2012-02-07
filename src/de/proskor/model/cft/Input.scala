package de.proskor.model.cft

trait Input extends Port with Source {
  override def toString = "input " + name + { source match {
    case Some(s: Source) => " {\n  ref " + s.elementName + ";\n}"
    case _ => ";"
  }}
  def matches(that: Input) = this.elementName == that.elementName
}