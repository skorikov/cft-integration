package de.proskor.cft.test

import de.proskor.automation._
import org.junit.Assert.{assertEquals, assertNotNull, assertFalse, assertTrue}
import org.junit.{AfterClass, Before, BeforeClass, Test, Ignore}

class AdapterTests {
  @Before
  def clear() = AdapterTests.clearRepository()

  @Test
  def testRepository() {
    assertNotNull(repository)
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
    models.remove(pkg)
    assertTrue(models.isEmpty)
    assertFalse(models.contains(pkg))
  }

  @Test
  def testPackages() {
    val name = "SUBPKG"
    val mod = model("PKG")
    val pk = pkg(mod, name)
    assertNotNull(pk)
    assertEquals(Some(mod), pk.parent)
    assertEquals(name, pk.name)
  }

  @Test
  def testElements() {
    val name = "FOOBAR"
    val mod = model("PKG")
    val sub = pkg(mod, "SUBPKG")
    val elements = sub.elements
    assertNotNull(elements)
    assertTrue(elements.isEmpty)
    val element = elements.add(name, "Object")
    assertNotNull(element)
    assertEquals(name, element.name)
    assertTrue(elements.contains(element))
    assertEquals(None, element.parent)
    assertEquals(sub, element.pkg)
    elements.remove(element)
    assertTrue(elements.isEmpty)
  }

  @Test
  def testElementAuthor() {
    val mod = model("MODEL")
    val pk = pkg(mod, "PKG")
    val element = obj(pk, "E1")
    element.author = "andrey"
    assertEquals("andrey", element.author)
  }

  @Test
  def testElementDescription() {
    val mod = model("MODEL")
    val pk = pkg(mod, "PKG")
    val element = obj(pk, "E1")
    element.description = "foobar"
    assertEquals("foobar", element.description)
  }

  @Test
  def testKids() {
    val mod = model("test")
    val element = obj(mod, "foo")
    val kid = obj(element, "bar")
    assertNotNull(kid)
    assertEquals(1, element.elements.size)
    assertEquals(Some(element), kid.parent)
    element.elements.remove(kid)
    assertTrue(element.elements.isEmpty)
  }

  @Test
  def testDiagram() {
    val mod = model("PKG")
    val diagram = mod.diagrams.add("DIAGRAM", "")
    assertNotNull(diagram)
    assertEquals("DIAGRAM", diagram.name)
    val node = diagram.nodes.add("Node", "")
    assertNotNull(node)
  }

  @Test
  def testConnectors() {
    val mod = model("MODEL")
    val pk = pkg(mod, "P1")
    val element = obj(pk, "E1")
    val connector = element.connectors.add("C1", "Connector")
  }

  private def repository: Repository = Repository.instance
  private def models: Collection[Package] = Repository.instance.models
  private def model(name: String): Package = models.add(name, "Package")
  private def pkg(parent: Package, name: String): Package = parent.packages.add(name, "Package")
  private def obj(parent: Container, name: String): Element = parent.elements.add(name, "Object")
}

object AdapterTests {
  @BeforeClass
  def configure() {
  //  Repository.instance = de.proskor.automation.impl.dummy.DummyRepository
  }

  @AfterClass
  def clearRepository() {
    Repository.instance.models.clear()
  }
}