package de.proskor.core
import de.proskor.model.Element
import de.proskor.model.cft.Component

class MergeTrace(val element: Element, val sources: List[Element], val conflict: Boolean) {
  var kids = List[MergeTrace]()
  def hasConflicts: Boolean = conflict || kids.exists(_.hasConflicts)
  override def toString = {
    element.elementName + " (" + sources.map(_.fullName).mkString(" + ") + ")" +
      (if (kids.isEmpty)
        ";"
      else
        " {\n" + indent(kids.map(_.toString).mkString("\n")) + "\n}")
  }

  private def indent(x: String) = "  " + x.replaceAll("\n", "\n  ")
}

object MergeTrace {
  def calculate(element: Element, trace: Map[Element, List[Element]], conflicts: List[Element]): MergeTrace = {
    val result = new MergeTrace(element, trace(element), conflicts.contains(element))
    val kids: List[Element] = element match {
      case component: Component => component.components ::: component.inputs ::: component.outputs ::: component.gates ::: component.events
      case _ => List()
    }
    for (kid <- kids) {
      result.kids ::= calculate(kid.asInstanceOf[Element], trace, conflicts)
    }
    result
  }
}