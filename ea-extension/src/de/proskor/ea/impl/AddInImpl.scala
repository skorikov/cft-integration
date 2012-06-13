package de.proskor.ea.impl

import de.proskor.automation.impl.RepositoryImpl
import de.proskor.cft.model.ea.EAFactory
import de.proskor.cft.model.Factory
import de.proskor.ea.AddIn
import de.proskor.ea.Extension
import cli.EA.IRepository
import de.proskor.ea.menu.MenuProvider
import de.proskor.ea.menu.Submenu
import de.proskor.ea.menu.MenuItem
import de.proskor.ea.menu.Menu
import de.proskor.ea.menu.MenuCommand

trait AddInImpl extends AddIn with Extension {
  var outputReady = false
  var repositoryPeer: cli.EA.IRepository = null

  val MainMenu = "-CFT Extension"
  val RunTestsMenuItem = "Run Tests"
  val MergeMenuItem = "Merge..."
  val OutputTitle = "CFT Extension"

  def EA_OnPostInitialized(repository: IRepository) {
    repositoryPeer = repository
    RepositoryImpl.peer = repository
    Factory.use(EAFactory)
    start()
  }

  def EA_Disconnect {
    stop()
  }

/*  def EA_GetMenuItems(repository: IRepository, location: String, menuName: String): AnyRef = menuName match {
    case "" => MainMenu
    case MainMenu => Array(RunTestsMenuItem, MergeMenuItem)
  }*/
  def EA_GetMenuItems(repository: IRepository, location: String, menuName: String): AnyRef = {
    val menu = getMenu(location)
    menuName match {
    //  case null => menu.items.map(_.name).toArray
      case "" => menu.items.map(_.name).toArray
      case item: String => (findMenuItem(menu, menuName) map {
        case sm: Submenu => sm.items.map(_.name).toArray
        case _ => null
      }).get
    }
  }

  private def getMenu(location: String): Menu = location match {
    case "MainMenu" => getMenuProvider.mainMenu
    case "TreeView" => getMenuProvider.treeMenu
    case "Diagram" => getMenuProvider.diagramMenu
  }

  private def findMenuItem(menu: Menu, name: String): Option[MenuItem] =
    menu.items.map(item => findItem(item, name)).collectFirst {
      case Some(item) => item
  }

  private def findItem(item: MenuItem, name: String): Option[MenuItem] =
    if (item.name == name)
      Some(item)
    else item match {
      case sm: Submenu => findMenuItem(sm, name)
      case _ => None
    }
      

  private def findMenuItem(name: String): Option[MenuItem] =
    findMenuItem(getMenuProvider.mainMenu, name).orElse(findMenuItem(getMenuProvider.treeMenu, name)).orElse(findMenuItem(getMenuProvider.diagramMenu, name))

/*  def EA_MenuClick(repository: IRepository, menuName: String, itemName: String): Unit = itemName match {
    case RunTestsMenuItem => tests()
    case MergeMenuItem => merge()
    case _ =>
  }*/
  def EA_MenuClick(repository: IRepository, menuName: String, itemName: String): Unit = findMenuItem(itemName).foreach {
    case mc: MenuCommand => mc.invoke
  }

  def write(text: String) = withVisibleOutput {
    repositoryPeer.WriteOutput(OutputTitle, text, 1)
  }

  private def withVisibleOutput(f: => Unit) {
    if (!outputReady) {
      repositoryPeer.CreateOutputTab(OutputTitle)
      outputReady = true
    } 
    repositoryPeer.EnsureOutputVisible(OutputTitle)
    f
  }
}