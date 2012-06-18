package de.proskor.ea.impl

import de.proskor.ea.TestRunner
import org.junit.runner.notification.RunNotifier
import org.junit.runner.Runner
import org.junit.runners.Suite
import org.junit.runner.notification.Failure
import org.junit.internal.builders.AllDefaultPossibilitiesBuilder
import org.junit.runners.JUnit4
import de.proskor.ea.Writable

trait TestRunnerImpl extends TestRunner {
  this: Writable =>

  def test(clazz: Class[_]): Unit = try {
    testRunner(clazz).run(testNotifier)
  } catch {
    case e => e.getStackTraceString.split("\n").map(_.trim).map(write)
  }

  def testRunner(clazz: Class[_]): Runner =
  //  new Suite(clazz, new AllDefaultPossibilitiesBuilder(false))
    new JUnit4(clazz)

  def testNotifier = new RunNotifier() {
    override def fireTestFailure(failure: Failure) {
      write("---- TEST FAILED! " + failure.getDescription() + " ----")
      failure.getTrace.split("\n").map(_.trim).map(write)
      write("-" * 60)
    }
  }
}