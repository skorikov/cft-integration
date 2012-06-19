package de.proskor

import de.proskor.extension.ExtensionAdapter
import de.proskor.extension.MenuItemAdapter
import de.proskor.automation.Repository
import de.proskor.fel.impl.EventRepositoryImpl
import de.proskor.fel.EventRepository
import de.proskor.fel.ui.FailureEventListDialog
import de.proskor.fel.ui.FailureEventListImpl


class CftExtension extends ExtensionAdapter {
  override protected def createMenu = {
    val mainMenu = new MenuItemAdapter("Main")
    val fooMenu = new MenuItemAdapter(mainMenu, "Foo")
    new MenuItemAdapter(fooMenu, "Action") {
      override def invoke() {
        val repository = Repository.instance
        repository.write(repository.context.toString)
      }
    }
    new MenuItemAdapter(mainMenu, "Failure Event List...") {
      override def invoke() {
        val er: EventRepository = new EventRepositoryImpl(Repository.instance)
        val dialog: FailureEventListDialog = new FailureEventListImpl(er)
        dialog.showEventList
      }
    }
    mainMenu
  }
}