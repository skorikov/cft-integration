package de.proskor

import de.proskor.extension.AddInBridge
import de.proskor.automation.impl.RepositoryImpl
import cli.EA.IRepository

class Main extends AddInBridge with ExceptionHandler {
  /* initialize both repository models here */
  override def initialize(repository: IRepository) {
    super.initialize(repository)
    RepositoryImpl.peer = repository
  }

  override protected def createExtension = new CftExtension
}