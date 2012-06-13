package de.proskor.ea.impl.menu

import de.proskor.ea.menu.MenuCommand
import de.proskor.ea.menu.Action

abstract class MenuCommandImpl(name: String) extends MenuItemImpl(name) with MenuCommand {
  
}