package de.proskor

import java.util.{List => JavaList}
import collection.JavaConversions._
import de.proskor.extension.ExtensionAdapter
import de.proskor.extension.MenuItemAdapter
import de.proskor.automation.Repository
import de.proskor.fel.impl.EventRepositoryImpl
import de.proskor.fel.EventRepository
import de.proskor.fel.ui.FailureEventListDialog
import de.proskor.fel.ui.FailureEventListImpl
import de.proskor.extension.MenuItem
import de.proskor.automation.impl.RepositoryImpl
import de.proskor.cft.test.AdapterTests
import de.proskor.cft.test.PeerTests
import de.proskor.cft.test.CftTests

class CftExtension extends ExtensionAdapter {
  private val runner = new TestRunner(Repository.instance.write)

  override protected def createMenu = {
    val cftMenu = new MenuItemAdapter("CFT")

    new MenuItemAdapter(cftMenu, "Run Tests") {
      setEnabled(false)

      override def invoke {
        Repository.instance.write("---- RUNNING TESTS ----")
      //  runner.test(classOf[AdapterTests])
      //  runner.test(classOf[PeerTests])
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

    new MenuItemAdapter(cftMenu, "Create Event Instance") {
      override def isVisible: Boolean = hasChildren

      override def hasChildren: Boolean = this.getChildren.size > 0

      override def getChildren: JavaList[MenuItem] = {
        val er: EventRepository = new EventRepositoryImpl(Repository.instance)
        val items = for {
          container <- er.getEventTypeContainers
          event <- container.getEvents
        } yield new MenuItemAdapter(event.getName) {
          override def invoke() {
            Repository.instance.write("create event instance for '" + event.getName + "'")
          }
        }
        items
      }
    }

    cftMenu
  }
}