package de.proskor.automation

trait Package extends Identity with Named with Container {
  def packages: Collection[Package]
  def diagrams: Collection[Diagram]
  def parent: Option[Package]
}