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
    val model = repository.getModels.add("model", Package.PACKAGE)
    assertNotNull(model)
    assertTrue(model.isModel)
    assertEquals("model", model.getName)
    assertEquals("", model.getDescription)
    assertEquals("", model.getStereotype)
    assertEquals(1, repository.getModels.size)
    assertTrue(repository.getModels.contains(model))
    assertEquals(0, repository.getModels.indexOf(model))
    assertEquals(model, repository.getModels.get(0))
  }

  @Test
  def testModelClear() {
    assumeTrue(validRepository)
    val model = repository.getModels.add("model", Package.PACKAGE)
    repository.getModels.clear()
    assertTrue(repository.getModels.isEmpty)
  }

  @Test
  def testModelRemoval() {
    assumeTrue(validRepository)
    val model = repository.getModels.add("model", Package.PACKAGE)
    repository.getModels.remove(model)
    assertTrue(repository.getModels.isEmpty)
  }

  @Test
  def testModelRename() {
    assumeTrue(validRepository)
    val model = repository.getModels.add("model", Package.PACKAGE)
    model.setName("renamed")
    assertEquals("renamed", repository.getModels.get(0).getName)
  }

  @Test
  def testModelDescriptionChange() {
    assumeTrue(validRepository)
    val model = repository.getModels.add("model", Package.PACKAGE)
    model.setDescription("description")
    assertEquals("description", model.getDescription)
  }

  @Test
  def testPackageCreation() {
    assumeTrue(validRepository)
    val model = repository.getModels.add("model", Package.PACKAGE)
    val pkg = model.getPackages.add("pkg", Package.PACKAGE)
    assertNotNull(pkg)
    assertFalse(pkg.isModel)
    assertEquals(model, pkg.getParent)
    assertEquals("pkg", pkg.getName)
    assertEquals("", pkg.getDescription)
    assertEquals("", pkg.getStereotype)
    assertEquals(1, model.getPackages.size)
    assertTrue(model.getPackages.contains(pkg))
    assertEquals(0, model.getPackages.indexOf(pkg))
    assertEquals(pkg, model.getPackages.get(0))
  }

  @Test
  def testPackageElement() {
    assumeTrue(validRepository)
    val model = repository.getModels.add("model", Package.PACKAGE)
    val pkg = model.getPackages.add("pkg", Package.PACKAGE)
    val element = pkg.getElement
    assertNotNull(element)
  }

  @Test
  def testPackageRemoval() {
    assumeTrue(validRepository)
    val model = repository.getModels.add("model", Package.PACKAGE)
    val pkg = model.getPackages.add("pkg", Package.PACKAGE)
    model.getPackages.removeAt(0)
    assertTrue(model.getPackages.isEmpty)
  }

  @Test
  def testPackageStereotypeChange() {
    assumeTrue(validRepository)
    val model = repository.getModels.add("model", Package.PACKAGE)
    val pkg = model.getPackages.add("pkg", Package.PACKAGE);
    pkg.setStereotype("stereo")
    assertEquals("stereo", pkg.getStereotype)
  }

  @Test
  def testElementCreation() {
    assumeTrue(validRepository)
    val pkg = createPkg
    val element = createObj(pkg, "element")
    assertNotNull(element)
    assertEquals(1, pkg.getElements.size)
    assertTrue(pkg.getElements.contains(element))
    assertFalse(element.isChild)
    assertEquals(pkg, element.getPackage)
    assertEquals(Element.OBJECT, element.getType)
    assertEquals("element", element.getName)
    assertEquals("", element.getDescription)
    assertEquals("", element.getStereotype)
  }

  @Test
  def testElementRename() {
    assumeTrue(validRepository)
    val pkg = createPkg
    val element = createObj(pkg, "element")
    assumeTrue(element.getName == "element")
    element.setName("renamed")
    assertEquals("renamed", element.getName)
  }

  @Test
  def testElementDescriptionChange() {
    assumeTrue(validRepository)
    val pkg = createPkg
    val element = createObj(pkg, "element")
    assertEquals("", element.getDescription)
    element.setDescription("desc")
    assertEquals("desc", element.getDescription)
  }

  @Test
  def testElementStereotypeChange() {
    assumeTrue(validRepository)
    val pkg = createPkg
    val element = createObj(pkg, "element")
    assertEquals("", element.getStereotype)
    element.setStereotype("stereo")
    assertEquals("stereo", element.getStereotype)
  }

  @Test
  def testElementChildCreation() {
    assumeTrue(validRepository)
    val pkg = createPkg
    val element = createObj(pkg, "element")
    assertTrue(element.getElements.isEmpty)
    val kid = element.getElements.add("kid", Element.OBJECT)
    assertNotNull(kid)
    assertTrue(kid.isChild)
    assertEquals(element, kid.getParent)
    assertTrue(element.getElements.contains(kid))
    assertEquals(pkg, kid.getPackage)
    assertEquals("kid", kid.getName)
  }

  @Test
  def testElementRemoval() {
    assumeTrue(validRepository)
    val pkg = createPkg
    val element = createObj(pkg, "element")
    pkg.getElements.remove(element);
    assertTrue(pkg.getElements.isEmpty)
  }

  @Test
  def testConnectionCreation() {
    assumeTrue(validRepository)
    val pkg = createPkg
    val source = createObj(pkg, "source")
    val target = createObj(pkg, "target")
    val connector = source.connectTo(target)
    assertNotNull(connector)
    assertEquals("", connector.getName)
    assertEquals("", connector.getDescription)
    assertEquals("", connector.getStereotype)
    assertEquals(Connector.CONNECTOR, connector.getType)
    assertEquals(source, connector.getSource)
    assertEquals(target, connector.getTarget)
    assertTrue(source.getConnectors.contains(connector))
    assertTrue(target.getConnectors.contains(connector))
  }

  @Test
  def testConnectionRemoval() {
    assumeTrue(validRepository)
    val pkg = createPkg
    val source = createObj(pkg, "source")
    val target = createObj(pkg, "target")
    val connector = source.connectTo(target)
    source.getConnectors.remove(connector)
    assertFalse(source.getConnectors.contains(connector))
    assertTrue(source.getConnectors.isEmpty)
    assertFalse(target.getConnectors.contains(connector))
    assertTrue(target.getConnectors.isEmpty)
  }

  @Test
  def testConnectionRename() {
    assumeTrue(validRepository)
    val pkg = createPkg
    val source = createObj(pkg, "source")
    val target = createObj(pkg, "target")
    val connector = source.connectTo(target)
    connector.setName("foo")
    assertEquals("foo", connector.getName)
  }

  @Test
  def testConnectionDescriptionChange() {
    assumeTrue(validRepository)
    val pkg = createPkg
    val source = createObj(pkg, "source")
    val target = createObj(pkg, "target")
    val connector = source.connectTo(target)
    connector.setDescription("foo")
    assertEquals("foo", connector.getDescription)
  }

  @Test
  def testConnectionStereotypeChange() {
    assumeTrue(validRepository)
    val pkg = createPkg
    val source = createObj(pkg, "source")
    val target = createObj(pkg, "target")
    val connector = source.connectTo(target)
    connector.setStereotype("foo")
    assertEquals("foo", connector.getStereotype)
  }

  @Test
  def testConnectionTypeChange() {
    assumeTrue(validRepository)
    val pkg = createPkg
    val source = pkg.getElements.add("source", Element.OBJECT)
    val target = pkg.getElements.add("target", Element.OBJECT)
    val connector = source.connectTo(target)
    connector.setType(Connector.DEPENDENCY)
    assertEquals(Connector.DEPENDENCY, connector.getType)
  }

  private def repository = AutomationTests.repository
  private def createPkg = repository.getModels.add("model", Package.PACKAGE).getPackages.add("pkg", Package.PACKAGE)
  private def createObj(pkg: Package, name: String) = pkg.getElements.add(name, Element.OBJECT)

  private def validRepository =
    (repository != null) && (repository.getModels != null) && repository.getModels.isEmpty
}

object AutomationTests {
  var repository: Repository = null

//  @AfterClass
  def clearRepository() = repository.getModels().clear()
}