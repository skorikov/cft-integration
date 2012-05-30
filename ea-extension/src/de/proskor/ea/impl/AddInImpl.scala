package de.proskor.ea.impl

import de.proskor.automation.impl.RepositoryImpl
import de.proskor.cft.model.ea.EAFactory
import de.proskor.cft.model.Factory
import de.proskor.ea.AddIn
import de.proskor.ea.Extension
import cli.EA.IRepository

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

  def EA_GetMenuItems(repository: IRepository, location: String, menuName: String): AnyRef = menuName match {
    case "" => MainMenu
    case MainMenu => Array(RunTestsMenuItem, MergeMenuItem)
  }

  def EA_MenuClick(repository: IRepository, menuName: String, itemName: String): Unit = itemName match {
    case RunTestsMenuItem => tests()
    case MergeMenuItem => merge()
    case _ =>
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