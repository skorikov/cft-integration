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

class CftTests extends JUnitSuite {
  @Test
  def testRepository() {
    val repository = Repository("/")
    assertNotNull(repository)
  }

  @Before
  def clear() = CftTests.clearRepository()

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

  @Test
  def testComponentCreation() {
    val repository = Repository("/")
    val pkg = Package(repository, "P1")
    val component = Component(pkg, "C1")
    assertNotNull(component)
    assertEquals("C1", component.name)
    assertEquals(1, pkg.components.size)
    assertTrue(pkg.components.contains(component))
    assertEquals(Some(pkg), component.parent)
  }

  @Test
  def testComponentRemoval() {
    val repository = Repository("/")
    val pkg = Package(repository, "P1")
    val component = Component(pkg, "C1")
    pkg -= component
    assertTrue(pkg.components.isEmpty)
    assertEquals(None, component.parent)
  }

  @Test
  def testEventCreation() {
    val repository = Repository("/")
    val pkg = Package(repository, "P1")
    val component = Component(pkg, "C1")
    val event = Event(component, "BE1")
    assertNotNull(event)
    assertEquals("BE1", event.name)
    assertEquals(1, component.events.size)
    assertTrue(component.elements.contains(event))
    assertTrue(component.events.contains(event))
    assertEquals(Some(component), event.parent)
  }

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

  @Test
  def testEventMove() {
    val repository = Repository("/")
    val pkg = Package(repository, "P1")
    val c1 = Component(pkg, "C1")
    val c2 = Component(pkg, "C2")
    val event = Event(c1, "BE1")
    c2 += event
    assertTrue(c1.events.isEmpty)
    assertTrue(c2.events.contains(event))
    assertEquals(Some(c2), event.parent)
  }

  @Test
  def testAndCreation() {
    val repository = Repository("/")
    val pkg = Package(repository, "P1")
    val component = Component(pkg, "C1")
    val and = And(component, "G1")
    assertNotNull(and)
    assertEquals("G1", and.name)
    assertEquals(1, component.gates.size)
    assertTrue(component.gates.contains(and))
    assertTrue(component.gates.contains(and))
    assertEquals(Some(component), and.parent)
  }

  @Test
  def testGateConnection() {
    val repository = Repository("/")
    val pkg = Package(repository, "P1")
    val component = Component(pkg, "C1")
    val and = And(component, "G1")
    assertTrue(and.inputs.isEmpty)
    val event = Event(component, "BE1")
    and += event
    assertEquals(1, and.inputs.size)
    assertTrue(and.inputs.contains(event))
  }

  @Test
  def testGateConnectionRemoval() {
    val repository = Repository("/")
    val pkg = Package(repository, "P1")
    val component = Component(pkg, "C1")
    val and = And(component, "G1")
    val event = Event(component, "BE1")
    and += event
    and -= event
    assertEquals(0, and.inputs.size)
  }

  @Test
  def testOutportCreation() {
    val repository = Repository("/")
    val pkg = Package(repository, "P1")
    val component = Component(pkg, "C1")
    val event = Event(component, "BE1")
    val outport = Outport(component, "O1")
    assertEquals(None, outport.input)
    outport += event
    assertEquals(Some(event), outport.input)
    outport -= event
    assertEquals(None, outport.input)
  }

  @Test
  def testNestedComponents() {
    val repository = Repository("/")
    val pkg = Package(repository, "P1")
    val component = Component(pkg, "C1")
    val subcomponent = Component(component, "C2")
    assertTrue(component.components.contains(subcomponent))
    assertEquals(Some(component), subcomponent.parent)
  }

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
  @AfterClass
  def clearRepository() {
    val repository = Repository("/")
    for (pkg <- repository.packages) {
      repository -= pkg
    }
  }
}