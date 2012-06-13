package de.proskor.ea.menu

trait MenuProvider {
  def mainMenu: Menu
  def treeMenu: Menu
  def diagramMenu: Menu
}