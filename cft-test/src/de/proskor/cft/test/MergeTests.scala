package de.proskor.cft.test
import org.junit.Assert._
import org.junit._
import org.scalatest.junit.JUnitSuite
import de.proskor.cft.model.Component
import de.proskor.cft.model.Event
import de.proskor.cft.model.Package
import de.proskor.cft.model.Repository
import de.proskor.cft.model.And
import de.proskor.cft.model.Outport
import de.proskor.cft.merge.MergeAlgorithm
import de.proskor.cft.merge.MergeTrace
import de.proskor.cft.model.Inport
import de.proskor.cft.model.Or

class MergeTests extends JUnitSuite {
  @Ignore
  @Test
  def testMerge() {
    val repository = Repository("/")
    val model = Package(repository, "Model")
    val leftPkg = Package(model, "L")
    val rightPkg = Package(model, "R")
    val target = Package(model, "P2")
    val left = Component(leftPkg, "C1")
    val right = Component(rightPkg, "C1")

    val li1 = Inport(left, "I1")
    val li3 = Inport(left, "I3")
    val lb1 = Event(left, "B1")
    val lb2 = Event(left, "B2")
    val lg1 = Or(left, "G1"); lg1 += li1; lg1 += lb1
    val lg2 = And(left, "G2"); lg2 += li3; lg2 += lb2
    val lg3 = Or(left, "G3"); lg3 += lg1; lg3 += lg2
    val lo1 = Outport(left, "O1"); lo1 += lg1
    val lo2 = Outport(left, "O2")
    val lo3 = Outport(left, "O3")
    val lo6 = Outport(left, "O6"); lo6 += lg3

    val ri2 = Inport(right, "I2")
    val rb1 = Event(right, "B1")
    val rb2 = Event(right, "B2")
    val rb3 = Event(right, "B3")
    val rg1 = Or(right, "G1"); rg1 += ri2; rg1 += rb1; rg1 += rb2
    val ro2 = Outport(right, "O2"); ro2 += rb1
    val ro5 = Outport(right, "O5"); ro5 += rg1
    val ro4 = Outport(right, "O4"); ro4 += rb3

    val al = new MergeAlgorithm
    val trace = new MergeTrace
    val result = al.merge(left, right, target, trace)
  }

  @Ignore
  @Test
  def testOutports() {
    val repository = Repository("/")
    val model = Package(repository, "Model")
    val leftPkg = Package(model, "LEFT")
    val rightPkg = Package(model, "RIGHT")
    val target = Package(model, "OUTPUT")
    val left = Component(leftPkg, "C1")
    val right = Component(rightPkg, "C1")

    val lb1 = Event(left, "B1")
    val lb2 = Event(left, "B2")
    val lo1 = Outport(left, "O1"); lo1 += lb1
    val lo2 = Outport(left, "O2"); lo2 += lb2

    val rb2 = Event(right, "B2")
    val rb3 = Event(right, "B3")
    val ro2 = Outport(right, "O2"); ro2 += rb2
    val ro3 = Outport(right, "O3"); ro3 += rb3

    val al = new MergeAlgorithm
    val trace = new MergeTrace
    val result = al.merge(left, right, target, trace)

    assertEquals(3, result.events.size)
    assertEquals(3, result.outports.size)

    val ob1 = result.events.find(_.name == "B1").get
    val ob2 = result.events.find(_.name == "B2").get
    val ob3 = result.events.find(_.name == "B3").get
    val oo1 = result.outports.find(_.name == "O1").get
    val oo2 = result.outports.find(_.name == "O2").get
    val oo3 = result.outports.find(_.name == "O3").get

    assertEquals(Some(ob1), oo1.input)
    assertEquals(Some(ob2), oo2.input)
    assertEquals(Some(ob3), oo3.input)
  }

  @Before
  def clear() = MergeTests.clearRepository()

}

object MergeTests {
  @AfterClass
  def clearRepository() {
    val repository = Repository("/")
    for (pkg <- repository.packages) {
      repository -= pkg
    }
  }
}