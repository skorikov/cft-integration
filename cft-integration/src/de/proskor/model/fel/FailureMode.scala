package de.proskor.model.fel
import de.proskor.model.Element

trait FailureMode {
  val element: Element
  var name: String
  var description: String
  var author: String
}