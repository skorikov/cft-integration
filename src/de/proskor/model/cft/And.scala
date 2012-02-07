package de.proskor.model.cft

trait And extends Gate {
  override def toString = {
    "and " + name + " {" +
      indent(sources map ("\nref " + _.elementName + ";") mkString) +
    "\n}"
  }
  def indent(x: String) = "  " + x.replaceAll("\n", "\n  ")
}