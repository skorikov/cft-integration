package de.proskor.ea.model.fel
import de.proskor.model.fel.BasicFailureMode
import de.proskor.model.Element

class EABasicFailureMode(element: Element, iname: String, idescription: String, iauthor: String) extends EAFailureMode(element, iname, idescription, iauthor) with BasicFailureMode {
  override def toString = Array("basic", name, description, author).mkString("|")
}