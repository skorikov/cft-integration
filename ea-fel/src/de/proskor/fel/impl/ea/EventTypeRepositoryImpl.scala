package de.proskor.fel.impl.ea

import java.util.{List => JavaList}
import de.proskor.automation.Repository
import de.proskor.fel.EventTypeRepository
import de.proskor.fel.EventTypeContainer

object EventTypeRepositoryImpl extends EventTypeRepository {
  lazy val repository: Repository = Repository.instance

  def getContainers: JavaList[EventTypeContainer] = {
    null
  }
}