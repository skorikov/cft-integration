package de.proskor.model.cft
import de.proskor.model.Element

trait Source extends Element {
  def matches(that: Source): Boolean = this.elementName == that.elementName
  def targets: List[Element]
}