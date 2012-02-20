package de.proskor.cft.model.simple
import de.proskor.cft.model.Element
import de.proskor.cft.model.Container

private abstract class SimpleElement(var name: String) extends Element {
  var container: Option[Container] = None
  def parent = container

  override def toString = parent match {
    case Some(container) => container + "/" + name
    case None => name
  }
}