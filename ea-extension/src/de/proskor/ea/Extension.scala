package de.proskor.ea

import de.proskor.ea.menu.MenuProvider

trait Extension {
  def getMenuProvider(): MenuProvider
  def start(): Unit
  def stop(): Unit
  def merge(): Unit
  def tests(): Unit
}