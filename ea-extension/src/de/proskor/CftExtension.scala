package de.proskor

import de.proskor.extension.ExtensionAdapter
import de.proskor.extension.MenuItemAdapter
import de.proskor.automation.Repository

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
    mainMenu
  }
}