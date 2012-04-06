package de.proskor.cft.test
import org.junit.Assert._
import org.junit._
import org.scalatest.junit.JUnitSuite
import de.proskor.automation.Repository
import de.proskor.automation.Package

class AITests extends JUnitSuite {
  @Before
  def clear() = AITests.clearRepository()

  @Test
  def testRepository() {
    val name: String = "P1"
    assertTrue(Repository.models.isEmpty)
    val pkg = Repository.models.add(name, "Package")
    assertNotNull(pkg)
    assertEquals(pkg.name, name)
    assertEquals(None, pkg.parent)
    assertEquals(1, Repository.models.size)
    Repository.models.delete(pkg)
    assertTrue(Repository.models.isEmpty)
  }

  @Test
  def testSubpackages() {
    val pkg = Repository.models.add("P1", "Package")
    val kid = pkg.packages.add("P2", "Package")
    assertNotNull(kid)
    assertEquals(kid.name, "P2")
    assertEquals(Some(pkg), kid.parent)
    assertEquals(1, pkg.packages.size)
    pkg.packages.delete(kid)
    assertTrue(pkg.packages.isEmpty)
  }
}

object AITests {
  @AfterClass
  def clearRepository() {
    Repository.models.clear;
  }
}