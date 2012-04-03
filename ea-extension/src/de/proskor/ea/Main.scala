package de.proskor.ea
import de.proskor.cft.model.Repository
import de.proskor.cft.model.Package
import de.proskor.cft.model.Element
import de.proskor.cft.test.CftTests
import de.proskor.cft.test.MergeTests
import de.proskor.ea.ui.MergeDialog
import org.scalatest.junit.JUnitRunner
import org.scalatest.junit.JUnitRunner
import org.scalatest.Suite
import org.junit.runner.Description
import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunNotifier

class Main extends Extension with Adapter {
  def start() {}
  def stop() {}

  def test() {
    val repository = de.proskor.automation.Repository
    repository.peer = de.proskor.cft.model.ea.EAFactory.repositoryPeer
    for {
      model <- repository.models
      pkg <- deepPkg(model)
      diagram <- pkg.diagrams
    } write(diagram.name)
  }

  def deepPkg(pkg: de.proskor.automation.Package): Iterable[de.proskor.automation.Package] =
    pkg.packages ++ pkg.packages.flatMap(deepPkg)

  def testRunner(clazz: Class[_]) = new JUnitRunner(clazz.asInstanceOf[Class[Suite]])

  def testNotifier = new RunNotifier() {
    override def fireTestFailure(failure: Failure) {
      write("---- TEST FAILED! " + failure.getDescription() + " ----")
      failure.getTrace.split("\n").map(_.trim).map(write)
      write("-" * 60)
    }
  }

  def runTests() {
    testRunner(classOf[CftTests]).run(testNotifier)
    testRunner(classOf[MergeTests]).run(testNotifier)
    write("---- ALL TESTS DONE ----")
  }

  def merge() {
    new MergeDialog(Repository("/"))
  }

  def allPackages(pkg: Package): Set[Package] = pkg.packages ++ pkg.packages.flatMap(allPackages)

  def fullName(element: Element): String = element.parent match {
    case None => element.name
    case Some(parent) => fullName(parent) + "/" + element.name
  }
}