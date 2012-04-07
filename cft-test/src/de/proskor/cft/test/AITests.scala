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
    val repository = Repository.instance
    assertTrue(repository.models.isEmpty)
    val pkg = repository.models.add(name, "Package")
    assertNotNull(pkg)
    assertEquals(pkg.name, name)
    assertEquals(None, pkg.parent)
    assertEquals(1, repository.models.size)
    repository.models.delete(pkg)
    assertTrue(repository.models.isEmpty)
  }

  @Test
  def testSubpackages() {
    val repository = Repository.instance
    val pkg = repository.models.add("P1", "Package")
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
    Repository.instance.models.clear;
  }
}