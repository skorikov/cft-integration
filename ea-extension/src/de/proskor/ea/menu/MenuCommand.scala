package de.proskor.ea.menu

trait MenuCommand extends MenuItem {
  def invoke: Unit
}