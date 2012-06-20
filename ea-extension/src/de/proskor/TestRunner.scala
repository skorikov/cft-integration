package de.proskor

import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunNotifier
import org.junit.runners.JUnit4
import org.junit.runner.Runner

class TestRunner(write: String => Unit) {
  def test(clazz: Class[_]): Unit = try {
    testRunner(clazz).run(testNotifier)
  } catch {
    case e => e.getStackTraceString.split("\n").map(_.trim).map(write)
  }

  private def testRunner(clazz: Class[_]): Runner =
  //  new Suite(clazz, new AllDefaultPossibilitiesBuilder(false))
    new JUnit4(clazz)

  private def testNotifier = new RunNotifier() {
    override def fireTestFailure(failure: Failure) {
      write("---- TEST FAILED! " + failure.getDescription() + " ----")
      failure.getTrace.split("\n").map(_.trim).map(write)
      write("-" * 60)
    }
  }
}