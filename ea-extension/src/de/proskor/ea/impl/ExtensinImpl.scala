package de.proskor.ea.impl

import de.proskor.cft.model.Repository
import de.proskor.cft.test._
import de.proskor.ea.ui.MergeDialog
import de.proskor.ea.{Extension, TestRunner}
import de.proskor.ea.impl.menu.MenuCommandImpl
import de.proskor.ea.impl.menu.MenuProviderImpl
import de.proskor.ea.impl.menu.SubmenuImpl
import de.proskor.ea.Writable

class ExtensionImpl extends Extension with Writable with TestRunnerImpl {
  var wr: String => Unit = null
  override def write(text: String) = wr(text)
  override val getMenuProvider = new MenuProviderImpl
  val cftMenu = new SubmenuImpl("-CFT Integration")
  val testItem = new MenuCommandImpl("Test") {
    override def invoke = tests()
  }
  cftMenu.items :+= testItem
  getMenuProvider.mainMenu.items :+= cftMenu

  def start() = write("hello world")
  def stop() = write("goodbye world")
  def merge() = new MergeDialog(Repository("/"))
  def tests() {
    write("---- RUNNING TESTS ----")
    test(classOf[AdapterTests])
    test(classOf[PeerTests])
    test(classOf[CftTests])
    write("---- ALL TESTS DONE ----")
  } 
}