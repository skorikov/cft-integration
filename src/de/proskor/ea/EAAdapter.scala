package de.proskor.ea
import cli.EA._
import de.proskor.ea.model._
import de.proskor.Extension

trait EAAdapter {
  this: Extension =>

  val MainMenu = "-CFT Integration"
  val MergeMenuItem = "Merge..."
  val InitializeMenuItem = "Create Diagram"

  def EA_Connect(repository: IRepository) = ""

  def EA_Disconnect() = close()

  def EA_GetMenuItems(repository: IRepository, location: String, menuName: String): Any = menuName match {
    case "" => MainMenu
    case MainMenu => Array(MergeMenuItem, InitializeMenuItem)
  }

  def EA_MenuClick(repository: IRepository, menuName: String, itemName: String) = itemName match {
    case MergeMenuItem => merge(new EARepository(repository))
    case InitializeMenuItem => createDiagram(new EARepository(repository))
    case _ =>
  }
}