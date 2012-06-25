package de.proskor.automation

trait Repository {
  def models: Collection[Package]
  def context: Option[Identity]
  def diagram: Option[Diagram]
  def write(text: String)
}

object Repository {
  var instance: Repository = de.proskor.automation.impl.RepositoryImpl
}