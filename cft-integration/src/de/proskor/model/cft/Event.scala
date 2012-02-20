package de.proskor.model.cft
import de.proskor.model.Element

trait Event extends Element with Source {
  override def toString = "event " + name + ";"
  def matches(that: Event) = this.elementName == that.elementName
  def targets: List[Element]
}