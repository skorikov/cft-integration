package de.proskor.automation

trait Repository {
  def models: Collection[Package]
}

object Repository {
  val instance: Repository = de.proskor.automation.impl.RepositoryImpl
}