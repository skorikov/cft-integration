package de.proskor.cft.test

import de.proskor.automation._
import org.junit.Assert.{assertEquals, assertNotNull, assertFalse, assertTrue}
import org.junit.{AfterClass, Before, BeforeClass, Test}

class AdapterTests {
  @Before
  def clear() = AdapterTests.clearRepository()

  @Test
  def testRepository() {
    val repository: Repository = Repository.instance
    assertNotNull(repository)
    val models: Collection[Package] = repository.models
    assertNotNull(models)
    assertTrue(models.isEmpty)
  }

  @Test
  def testModels() {
    val name = "TEST"
    val repository = Repository.instance
    val models = repository.models
    val pkg = models.add(name, "Package")
    assertNotNull(pkg)
    assertEquals(name, pkg.name)
    assertEquals(1, models.size)
    assertTrue(models.contains(pkg))
    models.delete(pkg)
    assertTrue(models.isEmpty)
    assertFalse(models.contains(pkg))
  }
}

object AdapterTests {
  @BeforeClass
  def configure() {
    Repository.instance = de.proskor.automation.impl.dummy.DummyRepository
  }

  @AfterClass
  def clearRepository() {
    Repository.instance.models.clear()
  }
}