package de.proskor.ea.impl

import de.proskor.automation.impl.RepositoryImpl
import de.proskor.cft.model.ea.EAFactory
import de.proskor.cft.model.Factory
import de.proskor.ea.Extension
import cli.EA.IRepository
import de.proskor.ea.menu.MenuProvider
import de.proskor.ea.menu.Submenu
import de.proskor.ea.menu.MenuItem
import de.proskor.ea.menu.Menu
import de.proskor.ea.menu.MenuCommand
import de.proskor.automation.AddInAdapter
import de.proskor.ea.Writable

class AddInImpl(val extension: Extension) extends AddInAdapter with Writable {
  var outputReady = false
  var repositoryPeer: cli.EA.IRepository = null

  val MainMenu = "-CFT Extension"
  val RunTestsMenuItem = "Run Tests"
  val MergeMenuItem = "Merge..."
  val OutputTitle = "CFT Extension"

  override def initialize(repository: IRepository) {
    repositoryPeer = repository
    RepositoryImpl.peer = repository
    Factory.use(EAFactory)
    extension.start()
  }

  override def getMenuItems(repository: IRepository, location: String, menuName: String): Array[String] = {
    val menu = this.getMenu(location)
    menuName match {
      case "" => menu.items.map(_.name).toArray
      case item: String => (findMenuItem(menu, menuName) map {
        case sm: Submenu => sm.items.map(_.name).toArray
        case _ => Array[String]()
      }).get
    }
  }

  private def getMenu(location: String): Menu = location match {
    case "MainMenu" => extension.getMenuProvider.mainMenu
    case "TreeView" => extension.getMenuProvider.treeMenu
    case "Diagram" => extension.getMenuProvider.diagramMenu
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
      

  private def findMenuItem(name: String): Option[MenuItem] = {
    val mp = extension.getMenuProvider
    findMenuItem(mp.mainMenu, name).orElse(findMenuItem(mp.treeMenu, name)).orElse(findMenuItem(mp.diagramMenu, name))
  }

  override def menuItemClicked(repository: IRepository, location: String, menuName: String, itemName: String): Unit = findMenuItem(itemName).foreach {
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