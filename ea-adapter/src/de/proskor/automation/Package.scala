package de.proskor.automation

trait Package {
  def id: Int
  var name: String
  def packages: Collection[Package]
  def elements: Collection[Element]
  def diagrams: Collection[Diagram]
  def parent: Option[Package]
}