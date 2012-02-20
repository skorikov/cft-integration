package de.proskor.model.cft
import de.proskor.model.Element

trait Port extends Element with Source {
  def component: Component
  def source: Option[Source]
  def targets: List[Element]
  def +=(source: Source)
}