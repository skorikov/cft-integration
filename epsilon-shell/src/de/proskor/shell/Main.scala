package de.proskor.shell

import de.proskor.automation.Repository

object Main {
  def main(args: Array[String]) {
    Repository.instance = de.proskor.automation.impl.dummy.DummyRepository
    val repository = Repository.instance
    repository.models.add("Model", "Package")
    new EpsilonShell
  }
}