package de.proskor

import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunNotifier
import org.junit.runners.JUnit4
import org.junit.runner.Runner
import org.junit.runner.Description

class TestRunner(write: String => Unit) {
  def test(clazz: Class[_]): Unit = try {
    testRunner(clazz).run(testNotifier)
  } catch {
    case e => e.getStackTraceString.split("\n").map(_.trim).map(write)
  }

  private def testRunner(clazz: Class[_]): Runner =
    new JUnit4(clazz)

  private def testNotifier = new RunNotifier() {
    override def fireTestFailure(failure: Failure) {
      write("-- " + failure.getDescription().getMethodName() + ": TEST FAILED")
      this.printTrace(failure)
    }
    override def fireTestStarted(description: Description) {
      write("-- RUNNING: " + description.getMethodName()) 
    }
    override def fireTestAssumptionFailed(failure: Failure) {
      write("-- " + failure.getDescription.getMethodName() + ": ASSUMPTION FAILED")
      this.printTrace(failure)
    }
    private def printTrace(failure: Failure) {
      write(":: " + failure.getDescription().toString())
      failure.getTrace.split("\n").map(_.trim).map(":: " + _).map(write)
      write("-" * 60)
    }
  }
}