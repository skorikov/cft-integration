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

class CftExtension extends ExtensionAdapter {
  override protected def createMenu = {
    val cftMenu = new MenuItemAdapter("CFT")
    
    new MenuItemAdapter(cftMenu, "Failure Event List...") {
      override def invoke {
        val er: EventRepository = new EventRepositoryImpl(Repository.instance)
        val dialog: FailureEventListDialog = new FailureEventListImpl(er)
        dialog.showEventList
      }
    }

    new MenuItemAdapter(cftMenu, "Create Event Instance") {
      override def hasChildren: Boolean = this.getChildren.size > 0

      override def getChildren: JavaList[MenuItem] = {
        val er: EventRepository = new EventRepositoryImpl(Repository.instance)
        val items = for {
          container <- er.getEventTypeContainers
          event <- container.getEvents
        } yield new MenuItemAdapter(event.getName)
        items
      }
    }

    cftMenu
  }
}