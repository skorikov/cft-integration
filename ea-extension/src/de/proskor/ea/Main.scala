package de.proskor.ea
import de.proskor.cft.model.Repository
import de.proskor.cft.model.Package
import de.proskor.cft.model.Element
import de.proskor.cft.test.CftTests
import org.scalatest.junit.JUnitRunner
import org.junit.runner.Description
import org.junit.runner.notification.Failure

class Main extends Extension with Adapter {
  def start() {}
  def stop() {}

  def test() {
    val repository = Repository("/")
    val pkg = Package(repository, "TEST")
    val subpkg = Package(pkg, "SUB")
  }

  def runTests() {
    val runner = new org.scalatest.junit.JUnitRunner(classOf[de.proskor.cft.test.CftTests].asInstanceOf[Class[org.scalatest.Suite]])
    runner.run(new org.junit.runner.notification.RunNotifier() {
      override def fireTestFailure(failure: Failure) {
        write(" !!! TEST FAILED: " + failure.getDescription() + " !!!")
        failure.getTrace.split("\n").map(_.trim).map(write)
        write("-" * 60)
      }
    })
    val runner2 = new org.scalatest.junit.JUnitRunner(classOf[de.proskor.cft.test.MergeTests].asInstanceOf[Class[org.scalatest.Suite]])
    runner2.run(new org.junit.runner.notification.RunNotifier() {
      override def fireTestFailure(failure: Failure) {
        write(" !!! TEST FAILED: " + failure.getDescription() + " !!!")
        failure.getTrace.split("\n").map(_.trim).map(write)
        write("-" * 60)
      }
    })
    write("ALL TESTS DONE")
  //  runner.
  }

  def allPackages(pkg: Package): Set[Package] = pkg.packages ++ pkg.packages.flatMap(allPackages)

  def fullName(element: Element): String = element.parent match {
    case None => element.name
    case Some(parent) => fullName(parent) + "/" + element.name
  }
}