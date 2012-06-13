package de.proskor.automation

trait Connector extends Identity with Named with Stereotyped {
  var source: Element
  var target: Element
}