package de.proskor

import cli.EA.IRepository
import de.proskor.automation.AddIn
import de.proskor.automation.MenuState
import de.proskor.automation.Repository

trait ExceptionHandler extends AddIn {

  abstract override def getMenuItems(repository: IRepository, location: String, menu: String): Array[String] =
    try super.getMenuItems(repository, location, menu) catch { 
      case e: Exception => print(e); Array()
    }

  abstract override def getMenuState(repository: IRepository, location: String, menu: String, item: String): MenuState =
    try super.getMenuState(repository, location, menu, item) catch { 
      case e: Exception => print(e); new MenuState(true, false)
    }

  abstract override def menuItemClicked(repository: IRepository, location: String, menu: String, item: String) =
    try super.menuItemClicked(repository, location, menu, item) catch { 
      case e: Exception => print(e)
    }

  private def print(e: Throwable) {
    val repository = Repository.instance
    repository.write(e.toString + ": " + e.getLocalizedMessage)
    e.getStackTraceString.split("\n").map(_.trim).map(repository.write)
    val cause = e.getCause
    if (e.getCause != null) {
      repository.write("Caused by:")
      print(e.getCause)
    }
  }
}