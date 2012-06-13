package de.proskor.ea.impl.menu

import de.proskor.ea.menu.MenuProvider

class MenuProviderImpl extends MenuProvider {
  val mainMenu: MenuImpl = new MenuImpl
  val treeMenu: MenuImpl = new MenuImpl
  val diagramMenu: MenuImpl = new MenuImpl
}