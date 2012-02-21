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

class MergeTests extends JUnitSuite {
  @Test
  def testMerge() {
    val repository = Repository("/")
    val sources = Package(repository, "P1")
    val target = Package(repository, "P2")
    val left = Component(sources, "C1")
    val e1 = Event(left, "BE1")
    val right = Component(sources, "C1")

    val al = new MergeAlgorithm
    val trace = new MergeTrace
    val result = al.merge(left, right, target, trace)
    assertTrue(target.components.contains(result))
    assertEquals(1, target.components.size)
    assertEquals(1, result.events.size)
    val eventCopy = result.events.head
    assertEquals("BE1", eventCopy.name)
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