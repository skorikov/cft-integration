package de.proskor.cft.test

import de.proskor.automation._
import org.junit.Assert.{assertEquals, assertNotNull, assertFalse, assertTrue}
import org.junit.{AfterClass, Before, BeforeClass, Test, Ignore}

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
    assertEquals(None, pkg.parent)
    assertEquals(name, pkg.name)
    assertEquals(1, models.size)
    assertTrue(models.contains(pkg))
    models.delete(pkg)
    assertTrue(models.isEmpty)
    assertFalse(models.contains(pkg))
  }

  @Test
  def testPackages() {
    val name = "SUBPKG"
    val models = Repository.instance.models
    val model = models.add("PKG", "Package")
    val pkg = model.packages.add(name, "Package")
    assertNotNull(pkg)
    assertEquals(Some(model), pkg.parent)
    assertEquals(name, pkg.name)
  }

  @Test
  def testElements() {
    val name = "FOOBAR"
    val models = Repository.instance.models
    val model = models.add("PKG", "Package")
    val pkg = model.packages.add("SUBPKG", "Package")
    val elements = pkg.elements
    assertNotNull(elements)
    assertTrue(elements.isEmpty)
    val element = elements.add(name, "Object")
    assertNotNull(element)
    assertEquals(name, element.name)
    assertTrue(elements.contains(element))
    assertEquals(None, element.parent)
    assertEquals(pkg, element.pkg)
    elements.delete(element)
    assertTrue(elements.isEmpty)
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