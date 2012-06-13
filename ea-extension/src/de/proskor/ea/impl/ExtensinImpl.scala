package de.proskor.ea.impl

import de.proskor.cft.model.Repository
import de.proskor.cft.test._
import de.proskor.ea.ui.MergeDialog
import de.proskor.ea.{AddIn, Extension, TestRunner}

abstract class ExtensionImpl extends Extension with TestRunner {
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