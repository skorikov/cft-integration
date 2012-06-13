package de.proskor.ea

import de.proskor.ea.menu.MenuProvider

trait Extension extends Writable {
  def getMenuProvider(): MenuProvider
  def start(): Unit
  def stop(): Unit
  def merge(): Unit
  def tests(): Unit
}