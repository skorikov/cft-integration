package de.proskor.ea.impl

import de.proskor.automation.AddInAdapter
import cli.EA.IRepository
import de.proskor.automation.MenuState

class DummyAddIn extends AddInAdapter {
  override def initialize(repository: IRepository) {
    repository.CreateOutputTab("NEW");
    repository.EnsureOutputVisible("NEW");
    repository.WriteOutput("NEW", "HELLO!", 0);
  }

  override def getMenuItems(repository: IRepository, location: String, menu: String) = location match {
    case "MainMenu" => menu match {
      case "" => Array("-WRAPPER")
      case "-WRAPPER" => Array("Foo", "Bar")
      case _ => Array()
    }
    case _ => Array()
  }

  override def getMenuState(repository: IRepository, location: String, menu: String, item: String) = location match {
    case "MainMenu" => item match {
      case "Foo" => new MenuState(true, true)
      case "Bar" => new MenuState(false, false)
    }
    case _ => super.getMenuState(repository, location, menu, item)
  }

  override def menuItemClicked(repository: IRepository, location: String, menu: String, item: String) = location match {
    case "MainMenu" => item match {
      case "Foo" => repository.WriteOutput("NEW", "Foo clicked!", 0)
    }
    case _ => super.menuItemClicked(repository, location, menu, item)
  }
}