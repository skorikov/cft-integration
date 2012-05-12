package de.proskor.cft.test

import de.proskor.cft.model._
import de.proskor.redundancy.RedundancyRemoval
import org.junit.Assert.{assertEquals, assertNotNull, assertFalse, assertTrue}
import org.junit.{AfterClass, Before, BeforeClass, Test, Ignore}

class RedundancyTests {
  @Before
  def clear() = RedundancyTests.clearRepository()

  @Test
  def testEquivalence() {
    val repository = Repository("/")
    val pkg = Package(repository, "PKG")
    val component = Component(pkg, "C1")
    val a = Event(component, "A")
    val b = Event(component, "B")
    assertTrue(RedundancyRemoval.equivalent(a, a))
    assertFalse(RedundancyRemoval.equivalent(a, b))
  }

  @Test
  def testEqualGates() {
    val repository = Repository("/")
    val pkg = Package(repository, "PKG")
    val component = Component(pkg, "C1")
    val a = Event(component, "A")
    val b = Event(component, "B")
    val f = And(component, "F"); f += a; f += b;
    val g = And(component, "G"); g += b; g += a;
    assertTrue(RedundancyRemoval.equivalent(f, g))
  }

  @Test
  def testNotEqualGates() {
    val repository = Repository("/")
    val pkg = Package(repository, "PKG")
    val component = Component(pkg, "C1")
    val a = Event(component, "A")
    val b = Event(component, "B")
    val f = And(component, "F"); f += a;
    val g = And(component, "G"); g += b;
    assertFalse(RedundancyRemoval.equivalent(f, g))
  }
}

object RedundancyTests {
  @BeforeClass
  def configure() {
    Factory.use(de.proskor.cft.model.simple.SimpleFactory)
  }

  @AfterClass
  def clearRepository() {
    val repository = Repository("/")
    for (pkg <- repository.packages) {
      repository -= pkg
    }
  }
}