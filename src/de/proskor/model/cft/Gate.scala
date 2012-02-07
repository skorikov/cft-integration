package de.proskor.model.cft
import de.proskor.model.Element

trait Gate extends Element with Source {
  def sources: List[Source]
  def +=(source: Source)
}