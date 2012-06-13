package de.proskor.ea.impl.menu

import de.proskor.ea.menu.MenuCommand

abstract class MenuCommandImpl(name: String) extends MenuItemImpl(name) with MenuCommand

