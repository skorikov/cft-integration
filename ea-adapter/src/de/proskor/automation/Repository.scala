package de.proskor.automation

trait Repository {
  def models: Collection[Package]
  def context: Option[Identity]
  def write(text: String)
}

object Repository {
  var instance: Repository = de.proskor.automation.impl.RepositoryImpl
}