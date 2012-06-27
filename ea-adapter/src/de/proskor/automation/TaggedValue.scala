package de.proskor.automation

trait TaggedValue extends Identity with Named {
  def element: Element
  var value: String
}