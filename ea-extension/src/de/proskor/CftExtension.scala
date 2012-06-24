package de.proskor

import java.util.{List => JavaList}

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.seqAsJavaList

import de.proskor.automation.Repository
import de.proskor.cft.test.AdapterTests
import de.proskor.cft.test.CftTests
import de.proskor.cft.test.PeerTests
import de.proskor.extension.ExtensionAdapter
import de.proskor.extension.MenuItem
import de.proskor.extension.MenuItemAdapter
import de.proskor.fel.impl.EventRepositoryImpl
import de.proskor.fel.ui.FailureEventListDialog
import de.proskor.fel.ui.FailureEventListImpl
import de.proskor.fel.EventRepository
import de.proskor.shell.EpsilonShell

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
      override def isVisible: Boolean = hasChildren

      override def getChildren: JavaList[MenuItem] = for {
        container <- eventRepository.getEventTypeContainers
        event <- container.getEvents
      } yield new MenuItemAdapter(event.getName) {
        override def invoke() {
          Repository.instance.write("create event instance for '" + event.getName + "'")
        }
      }

      lazy val eventRepository: EventRepository = new EventRepositoryImpl(Repository.instance)
    }

    cftMenu
  }
}