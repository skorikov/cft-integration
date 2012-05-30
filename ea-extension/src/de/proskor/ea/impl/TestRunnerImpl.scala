package de.proskor.ea.impl

import de.proskor.ea.TestRunner
import org.junit.runner.notification.RunNotifier
import org.junit.runner.Runner
import org.junit.runners.Suite
import org.junit.runner.notification.Failure
import org.junit.internal.builders.AllDefaultPossibilitiesBuilder

trait TestRunnerImpl extends TestRunner {
  def test(clazz: Class[_]): Unit =
    testRunner(clazz).run(testNotifier)

  def testRunner(clazz: Class[_]): Runner =
    new Suite(clazz, new AllDefaultPossibilitiesBuilder(false))

  def testNotifier = new RunNotifier() {
    override def fireTestFailure(failure: Failure) {
      write("---- TEST FAILED! " + failure.getDescription() + " ----")
      failure.getTrace.split("\n").map(_.trim).map(write)
      write("-" * 60)
    }
  }
}