package de.proskor.automation

trait Repository {
  def models: Collection[Package]
}

object Repository {
  var instance: Repository = de.proskor.automation.impl.RepositoryImpl
}