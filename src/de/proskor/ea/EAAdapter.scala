package de.proskor.ea
import cli.EA.IRepository
import de.proskor.ea.model.EARepository
import de.proskor.Extension
import de.proskor.model.Repository

trait EAAdapter {
  this: Extension =>

  val MainMenu = "-CFT Integration"
  val MergeMenuItem = "Merge..."
  val DiagramMenuItem = "Create Diagram"
  val FailureModesMenuItem = "Show Failure Modes"
  val PrintIdMenuItem = "Print Element Id"
  val TestMenuItem = "Test"

  def EA_OnPostInitialized(repository: IRepository) {
    Repository.current = Some(new EARepository(repository))
    start()
  }

  def EA_Disconnect {
    stop()
    EADataBase.saveFailureModes()
  }

  def EA_GetMenuItems(repository: IRepository, location: String, menuName: String): Any = menuName match {
    case "" => MainMenu
    case MainMenu => Array(MergeMenuItem, DiagramMenuItem, FailureModesMenuItem, PrintIdMenuItem, TestMenuItem)
  }

  def EA_MenuClick(repository: IRepository, menuName: String, itemName: String) = itemName match {
    case MergeMenuItem => merge()
    case DiagramMenuItem => createDiagram()
    case FailureModesMenuItem => failureModes()
    case PrintIdMenuItem => printId()
    case TestMenuItem => test()
    case _ =>
  }
}