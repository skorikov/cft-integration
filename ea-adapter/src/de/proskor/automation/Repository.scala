package de.proskor.automation

trait Repository extends Writable {
  def models: Collection[Package]
  def context: Option[Identity]
  def diagram: Option[Diagram]
}

object Repository {
  var instance: Repository = de.proskor.automation.impl.RepositoryImpl
}