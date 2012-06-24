package de.proskor

import java.util.{List => JavaList}
import scala.collection.JavaConversions._
import de.proskor.automation.Element
import de.proskor.automation.Repository
import de.proskor.cft.test.AdapterTests
import de.proskor.cft.test.CftTests
import de.proskor.cft.test.PeerTests
import de.proskor.extension.ExtensionAdapter
import de.proskor.extension.MenuItem
import de.proskor.extension.MenuItemAdapter
import de.proskor.fel.container.EventInstanceContainer
import de.proskor.fel.event.EventType
import de.proskor.fel.impl.EventInstanceContainerImpl
import de.proskor.fel.impl.EventRepositoryImpl
import de.proskor.fel.ui.FailureEventListDialog
import de.proskor.fel.ui.FailureEventListImpl
import de.proskor.fel.EventRepository
import de.proskor.shell.EpsilonShell
import java.util.Collections
import de.proskor.fel.container.EventTypeContainer
import de.proskor.fel.impl.EventTypeContainerImpl
import de.proskor.fel.impl.EventTypeImpl

class CftExtension extends ExtensionAdapter {
  private val runner = new TestRunner(Repository.instance.write)

  override protected def createMenu = {
    val cftMenu = new MenuItemAdapter("CFT")

    new MenuItemAdapter(cftMenu, "Run Tests") {
//      setEnabled(false)
      override def invoke {
        Repository.instance.write("---- RUNNING TESTS ----")
        runner.test(classOf[AdapterTests])
        runner.test(classOf[PeerTests])
        runner.test(classOf[CftTests])
        Repository.instance.write("---- ALL TESTS DONE ----")
      }
    }

    new MenuItemAdapter(cftMenu, "Failure Event List...") {
      override def invoke {
        val er: EventRepository = new EventRepositoryImpl(Repository.instance)
        val dialog: FailureEventListDialog = new FailureEventListImpl(er)
        dialog.showEventList
      }
    }

    new MenuItemAdapter(cftMenu, "Epsilon Shell...") {
      override def invoke {
        new EpsilonShell
      }
    }

    new MenuItemAdapter(cftMenu, "Create Event Instance") {
      override def isVisible = hasChildren

      override def getChildren: JavaList[MenuItem] = for (event <- eventTypes) yield new MenuItemAdapter(event.getName) {
        override def invoke {
          repository.write("create event instance for '" + event.getName + "'")
          val container = repository.context.get.asInstanceOf[Element]
          val eventInstance = container.elements.add(event.getName, "Object")
          eventInstance.stereotype = "Event"
          val connector = eventInstance.connectors.add("", "Connector")
          connector.source = eventInstance
          connector.target = container
          connector.stereotype = "belongsTo"
          val c2 = eventInstance.connectors.add("", "Connector")
          c2.source = eventInstance
          c2.target = event.asInstanceOf[EventTypeImpl].peer
          c2.stereotype = "instanceOf"
        }
      }

      private def repository = Repository.instance

      private def selectedContainer: Option[EventInstanceContainer] = repository.context collect {
        case element: Element if element.stereotype == "EventInstanceContainer" => new EventInstanceContainerImpl(element)
      }

      private def eventTypes: JavaList[EventType] = selectedContainer map { _.getType.getEvents } getOrElse Collections.emptyList()
    }

    cftMenu
  }
}