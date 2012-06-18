package de.proskor.ea

import de.proskor.automation.AddIn
import cli.EA.IRepository

trait AddInBridge {
  this: AddIn =>

  def EA_Connect(repository: IRepository) {
    this.start()
  }

  def EA_OnPostInitialized(repository: IRepository) {
    this.initialize(repository)
  }

  def EA_Disconnect() {
    this.stop()
  }

  def EA_GetMenuItems(repository: IRepository, location: String, name: String) = {
    this.getMenuItems(repository, location, name)
  }

  def EA_MenuClick(repository: IRepository, location: String, menu: String, item: String) {
    this.menuItemClicked(repository, location, menu, item)
  }
}