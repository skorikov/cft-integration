package de.proskor.cft.test

import de.proskor.automation._
import org.junit.Assert.{assertEquals, assertNotNull, assertFalse, assertTrue}
import org.junit.{AfterClass, Before, BeforeClass, Test, Ignore}
import de.proskor.fel.EventRepository
import de.proskor.fel.ui.FailureEventListDialog
import de.proskor.fel.ui.FailureEventListImpl
import de.proskor.fel.impl.EventRepositoryImpl

class FELTests {
  @Before
  def clear() = FELTests.clearRepository()

  @Test
  def testFEL() {
    val mod = model("FEL")
    val pk = pkg(mod, "System")
    
    val ob = obj(pk, "Test")
    ob.stereotype = "EventTypeContainer"

    val et = obj(mod, "EvTyp")
    et.stereotype = "EventType"
    val c1 = et.connectors.add("C", "Connector"); c1.stereotype = "belongsTo"
    c1.source = et
    c1.target = ob

    val cont = obj(pk, "Container")
    cont.stereotype = "EventInstanceContainer"
    val cc = cont.connectors.add("C", "Connector"); cc.stereotype = "instanceOf"
    cc.source = cont
    cc.target = ob

    val event = obj(cont, "Event")
    event.stereotype = "Event"
    val ct = cont.connectors.add("C", "Connector"); ct.stereotype = "instanceOf"
    ct.source = event
    ct.target = et

    val er: EventRepository = new EventRepositoryImpl(repository)
    assertEquals(1, er.getEventTypeContainers.size)

    val etc1 = er.getEventTypeContainers.get(0)
    assertEquals(1, etc1.getEvents.size)

    val dialog: FailureEventListDialog = new FailureEventListImpl(er)
    dialog.showEventList
  }

  @Test
  def testEventTypeContainers() {
    val mod = model("System")
    val etc = mod.elements.add("Foo", "Object"); etc.stereotype = "EventTypeContainer"
    val er: EventRepository = new EventRepositoryImpl(repository)
    assertEquals(1, er.getEventTypeContainers().size())
  }

  private def repository: Repository = Repository.instance
  private def models: Collection[Package] = Repository.instance.models
  private def model(name: String): Package = models.add(name, "Package")
  private def pkg(parent: Package, name: String): Package = parent.packages.add(name, "Package")
  private def obj(parent: Container, name: String): Element = parent.elements.add(name, "Object")
}

object FELTests {
  @BeforeClass
  def configure() {
    Repository.instance = de.proskor.automation.impl.dummy.DummyRepository
  }

  @AfterClass
  def clearRepository() {
    Repository.instance.models.clear()
  }
}