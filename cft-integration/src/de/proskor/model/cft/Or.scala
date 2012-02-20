package de.proskor.model.cft

trait Or extends Gate {
  override def toString = {
    "or " + name + " {" +
      indent(sources map ("\nref " + _.elementName + ";") mkString) +
    "\n}"
  }
  def indent(x: String) = "  " + x.replaceAll("\n", "\n  ")
}