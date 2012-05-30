package de.proskor.ea

import cli.EA.IRepository

trait AddIn {
  def EA_OnPostInitialized(repository: IRepository): Unit
  def EA_Disconnect(): Unit
  def EA_GetMenuItems(repository: IRepository, location: String, menuName: String): AnyRef
  def EA_MenuClick(repository: IRepository, menuName: String, itemName: String): Unit
}