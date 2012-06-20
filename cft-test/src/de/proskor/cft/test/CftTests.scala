package de.proskor.cft.test

import de.proskor.cft.model._
import org.junit.Assert.{assertTrue, assertEquals, assertNotNull}
import org.junit.{AfterClass, Before, BeforeClass, Test}
import org.junit.Ignore

class CftTests {
  @Before
  def clear() = CftTests.clearRepository()

  @Ignore
  @Test
  def testRepository() {
    val repository = Repository("/")
    assertNotNull(repository)
  }

  @Ignore
  @Test
  def testPackageCreation() {
    val repository = Repository("/")
    val pkg = Package("P1")
    assertNotNull(pkg)
    assertEquals("P1", pkg.name)
    assertEquals(None, pkg.parent)
    repository += pkg
    assertEquals(1, repository.packages.size)
    assertTrue(repository.packages.contains(pkg))
    assertEquals(Some(repository), pkg.parent)
  }

  @Ignore
  @Test
  def testPackageCreationWithParent() {
    val repository = Repository("/")
    val pkg = Package(repository, "P1")
    assertNotNull(pkg)
    assertEquals("P1", pkg.name)
    assertEquals(1, repository.packages.size)
    assertTrue(repository.packages.contains(pkg))
    assertEquals(Some(repository), pkg.parent)
  }

  @Ignore
  @Test
  def testPackageRemoval() {
    val repository = Repository("/")
    val pkg = Package(repository, "P1")
    assertEquals(1, repository.packages.size)
    assertTrue(repository.packages.contains(pkg))
    repository -= pkg
    assertTrue(repository.packages.isEmpty)
    assertEquals(None, pkg.parent)
  }

  @Ignore
  @Test
  def testSubPackageCreation() {
    val repository = Repository("/")
    val pkg = Package(repository, "P1")
    val subpkg = Package(pkg, "SP1")
    assertEquals(1, repository.packages.size)
    assertTrue(repository.packages.contains(pkg))
    assertTrue(repository.packages.forall(_.packages.size == 1))
    assertTrue(repository.packages.forall(_.packages.contains(subpkg)))
  }

  @Ignore
  @Test
  def testDeepPackageRemoval() {
    val repository = Repository("/")
    val pkg = Package(repository, "P1")
    val subpkg = Package(pkg, "SP1")
    assertEquals(1, repository.packages.size)
    assertTrue(repository.packages.contains(pkg))
    repository -= pkg
    assertTrue(repository.packages.isEmpty)
  }

  @Ignore
  @Test
  def testComponentCreation() {
    val repository = Repository("/")
    val model = Package(repository, "P1")
    val pkg = Package(model, "P2")
    val component = Component(pkg, "C1")
    assertNotNull(component)
    assertEquals("C1", component.name)
    assertEquals(1, pkg.components.size)
    assertTrue(pkg.components.contains(component))
    assertEquals(Some(pkg), component.parent)
  }

  @Ignore
  @Test
  def testChangePackageName() {
    val repository = Repository("/")
    val model = Package(repository, "P1")
    model.name = "P2"
    assertEquals("P2", model.name)
  }

  @Ignore
  @Test
  def testChangeComponentName() {
    val repository = Repository("/")
    val model = Package(repository, "P1")
    val pkg = Package(model, "P2")
    val component = Component(pkg, "C1")
    component.name = "C2"
    assertEquals("C2", component.name)
  }

  @Ignore
  @Test
  def testComponentRemoval() {
    val repository = Repository("/")
    val pkg = Package(repository, "P1")
    val component = Component(pkg, "C1")
    pkg -= component
    assertTrue(pkg.components.isEmpty)
    assertEquals(None, component.parent)
  }

  @Ignore
  @Test
  def testEventCreation() {
    val repository = Repository("/")
    val model = Package(repository, "P1")
    val pkg = Package(model, "P2")
    val component = Component(pkg, "C1")
    val event = Event(component, "BE1")
    assertNotNull(event)
    assertEquals("BE1", event.name)
    assertEquals(1, component.events.size)
    val elements = component.elements
    assertTrue(component.elements.contains(event))
    assertTrue(component.events.contains(event))
    assertEquals(Some(component), event.parent)
  }

  @Ignore
  @Test
  def testEventRemoval() {
    val repository = Repository("/")
    val pkg = Package(repository, "P1")
    val component = Component(pkg, "C1")
    val event = Event(component, "BE1")
    component -= event
    assertTrue(component.elements.isEmpty)
    assertEquals(None, event.parent)
  }

  @Ignore
  @Test
  def testEventMove() {
    val repository = Repository("/")
    val model = Package(repository, "P1")
    val pkg = Package(model, "P2")
    val c1 = Component(pkg, "C1")
    val c2 = Component(pkg, "C2")
    val event = Event(c1, "BE1")
    c2 += event
    assertTrue(c1.events.isEmpty)
    assertTrue(c2.events.contains(event))
    assertEquals(Some(c2), event.parent)
  }

  @Ignore
  @Test
  def testAndCreation() {
    val repository = Repository("/")
    val model = Package(repository, "P1")
    val pkg = Package(model, "P2")
    val component = Component(pkg, "C1")
    val and = And(component, "G1")
    assertNotNull(and)
    assertEquals("G1", and.name)
    assertEquals(1, component.gates.size)
    assertTrue(component.gates.contains(and))
    assertEquals(Some(component), and.parent)
  }

  @Test
  def testGateConnection() {
    val repository = Repository("/")
    val model = Package(repository, "P1")
    val pkg = Package(model, "P2")
    val component = Component(pkg, "C1")
    val and = And(component, "G1")
    assertTrue(and.inputs.isEmpty)
    val event = Event(component, "BE1")
    and += event // FIXME
    assertEquals(1, and.inputs.size)
    assertTrue(and.inputs.contains(event))
  }

  @Ignore
  @Test
  def testGateConnectionRemoval() {
    val repository = Repository("/")
    val pkg = Package(repository, "P1")
    val component = Component(pkg, "C1")
    val and = And(component, "G1")
    val event = Event(component, "BE1")
    and += event // FIXME
    and -= event
    assertEquals(0, and.inputs.size)
  }

  @Ignore
  @Test
  def testOutportCreation() {
    val repository = Repository("/")
    val pkg = Package(repository, "P1")
    val component = Component(pkg, "C1")
    val event = Event(component, "BE1")
    val outport = Outport(component, "O1")
    assertTrue(outport.inputs.isEmpty)
    outport += event // FIXME
    assertEquals(Set(event), outport.inputs)
    outport -= event
    assertTrue(outport.inputs.isEmpty)
  }

  @Ignore
  @Test
  def testNestedComponents() {
    val repository = Repository("/")
    val pkg = Package(repository, "P1")
    val component = Component(pkg, "C1")
    val subcomponent = Component(component, "C2")
    assertTrue(component.components.contains(subcomponent)) // FIXME
    assertEquals(Some(component), subcomponent.parent)
  }

  @Ignore
  @Test
  def testNestedComponentRemoval() {
    val repository = Repository("/")
    val pkg = Package(repository, "P1")
    val component = Component(pkg, "C1")
    val subcomponent = Component(component, "C2")
    component -= subcomponent
    assertTrue(component.components.isEmpty)
    assertEquals(None, subcomponent.parent)
  }
}

object CftTests {
  @BeforeClass
  def configure() {
//    de.proskor.automation.Repository.instance = de.proskor.automation.impl.dummy.DummyRepository
    de.proskor.cft.model.Factory.use(de.proskor.cft.model.ea.EAFactory)
  }

//  @AfterClass
  def clearRepository() {
    val repository = Repository("/")
    for (pkg <- repository.packages) {
      repository -= pkg
    }
  }
}