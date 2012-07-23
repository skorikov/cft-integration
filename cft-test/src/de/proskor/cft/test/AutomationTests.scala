package de.proskor.cft.test

import de.proskor.model._
import org.junit.Assume.{assumeTrue, assumeNotNull}
import org.junit.Assert.{assertEquals, assertNotNull, assertFalse, assertTrue}
import org.junit.{AfterClass, Before, BeforeClass, Test, Ignore}

class AutomationTests {
  @Before
  def clear() = AutomationTests.clearRepository()

  @Test
  def testRepository() {
    assertTrue(validRepository)
  }

  @Test
  def testModelCreation() {
    assumeTrue(validRepository)
    val model = repository.getModels.add("model", "Package")
    assertNotNull(model)
    assertTrue(model.isModel)
    assertEquals("model", model.getName)
    assertEquals(1, repository.getModels.size)
    assertTrue(repository.getModels.contains(model))
    assertEquals(0, repository.getModels.indexOf(model))
    assertEquals(model, repository.getModels.get(0))
  }

  @Test
  def testModelClear() {
    assumeTrue(validRepository)
    val model = repository.getModels.add("model", "Package")
    repository.getModels.clear()
    assertTrue(repository.getModels.isEmpty)
  }

  @Test
  def testModelRemoval() {
    assumeTrue(validRepository)
    val model = repository.getModels.add("model", "Package")
    repository.getModels.remove(0)
    assertTrue(repository.getModels.isEmpty)
  }

  @Test
  def testModelRename() {
    assumeTrue(validRepository)
    val model = repository.getModels.add("model", "Package")
    model.setName("renamed")
    assertEquals("renamed", repository.getModels.get(0).getName)
  }

  @Test
  def testModelDescriptionChange() {
    assumeTrue(validRepository)
    val model = repository.getModels.add("model", "Package")
    model.setDescription("description")
    assertEquals("description", model.getDescription)
  }

  @Test
  def testPackageCreation() {
    assumeTrue(validRepository)
    val model = repository.getModels.add("model", "Package")
    val pkg = model.getPackages.add("pkg", "Package")
    assertNotNull(pkg)
    assertFalse(pkg.isModel)
    assertEquals(model, pkg.getParent)
    assertEquals("pkg", pkg.getName)
    assertEquals(1, model.getPackages.size)
    assertTrue(model.getPackages.contains(pkg))
    assertEquals(0, model.getPackages.indexOf(pkg))
    assertEquals(pkg, model.getPackages.get(0))
  }

  @Test
  def testPackageRemoval() {
    assumeTrue(validRepository)
    val model = repository.getModels.add("model", "Package")
    val pkg = model.getPackages.add("pkg", "Package")
    model.getPackages.remove(0)
    assertTrue(model.getPackages.isEmpty)
  }

  @Test
  def testPackageStereotypeChange() {
    assumeTrue(validRepository)
    val model = repository.getModels.add("model", "Package")
    val pkg = model.getPackages.add("pkg", "Package");
    pkg.setStereotype("stereo")
    assertEquals("stereo", model.getStereotype)
  }

  private def repository = AutomationTests.repository

  private def validRepository =
    (repository != null) && (repository.getModels != null) && repository.getModels.isEmpty
}

object AutomationTests {
  var repository: Repository = null

//  @AfterClass
  def clearRepository() = repository.getModels().clear()
}