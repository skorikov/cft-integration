package de.proskor.utils

import cli.EA.IRepository
import de.proskor.automation.AddIn
import de.proskor.automation.MenuState

trait ExceptionHandler extends AddIn {

  abstract override def getMenuItems(repository: IRepository, location: String, menu: String): Array[String] =
    try super.getMenuItems(repository, location, menu) catch { 
      case e: Exception => print(e, repository); Array()
    }

  abstract override def getMenuState(repository: IRepository, location: String, menu: String, item: String): MenuState =
    try super.getMenuState(repository, location, menu, item) catch { 
      case e: Exception => print(e, repository); new MenuState(true, false)
    }

  abstract override def menuItemClicked(repository: IRepository, location: String, menu: String, item: String) =
    try super.menuItemClicked(repository, location, menu, item) catch { 
      case e: Exception => print(e, repository)
    }

  private def print(e: Throwable, repository: IRepository) {
    repository.CreateOutputTab("Stack Trace")
    repository.EnsureOutputVisible("Stack Trace")
    def write(text: String) {
      repository.WriteOutput("Stack Trace", text, 0);
    }
    write(e.toString + ": " + e.getLocalizedMessage + " (" + e.getMessage() + ")")
    e.getStackTraceString.split("\n").map(_.trim).map(write)
    val cause = e.getCause
    if (e.getCause != null) {
      write("Caused by:")
      print(e.getCause, repository)
    }
  }
}