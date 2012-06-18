package de.proskor.ea

import de.proskor.ea.impl.menu.MenuCommandImpl
import de.proskor.ea.impl.menu.MenuProviderImpl
import de.proskor.ea.impl.menu.SubmenuImpl
import de.proskor.ea.impl._
import de.proskor.automation.AddIn

class Main extends AddInImpl(new ExtensionImpl) with AddInBridge {
  this.extension.asInstanceOf[ExtensionImpl].wr = this.write
  
}

object Main {

  private trait WriteRedirect extends Writable {
    override def write(text: String) = println(text)
  }

  private val addin: AddIn = new Main with WriteRedirect

  private sealed abstract class MenuItem
  private case class RootItem(kids: Array[MenuItem]) extends MenuItem
  private case class SingleItem(name: String) extends MenuItem
  private case class CompositeItem(name: String, kids: Array[MenuItem]) extends MenuItem

  private def menu: MenuItem = {
    def clean(name: String): String = if (name.startsWith("-")) name.drop(1) else name

    def root(name: String): Boolean = name.isEmpty

    def expandable(name: String): Boolean = name.startsWith("-") || root(name)

    def item(name: String): MenuItem = if (expandable(name)) {

      def constructor(name: String, kids: Array[MenuItem]): MenuItem =
        if (root(name)) RootItem(kids) else CompositeItem(clean(name), kids)

      addin.getMenuItems(null, "MainMenu", name) match {
      //  case single: String => constructor(name, Array(item(single)))
        case array: Array[String] => constructor(name, array.map(item))
      }

    } else {
      SingleItem(name)
    }

    item("")
  }


  private def print(item: MenuItem, indent: Int = 0): Unit = item match {
    case RootItem(kids) => kids.map(print(_, indent))
    case SingleItem(name) => println(" `- " * indent + name)
    case CompositeItem(name, kids) => println(" `- " * indent + name); kids.map(print(_, indent + 1))
  }

  def main(args: Array[String]) {
    de.proskor.automation.Repository.instance = de.proskor.automation.impl.dummy.DummyRepository
    addin.initialize(null)
    print(menu)
    addin.menuItemClicked(null, "MainMenu", null, "Run Tests")
    addin.stop()
  }
}