package de.proskor.fel.impl

import java.util.{List => JavaList}
import collection.JavaConversions._
import de.proskor.fel.EventRepository;
import de.proskor.fel.event.EventType
import de.proskor.fel.container.EventTypeContainer
import de.proskor.automation.Package
import de.proskor.automation.Repository
import de.proskor.automation.Element

class EventRepositoryImpl(repository: Repository) extends EventRepository {
  private def felPackage: Package = {
    repository.models.find(_.name == "FEL") getOrElse repository.models.add("FEL", "Package")
  }

  override def getEventTypeContainers: JavaList[EventTypeContainer] = {
    repository.models.flatMap(allObjects).map(new EventTypeContainerImpl(_)).toList
  }

  private def allObjects(pkg: Package): Seq[Element] =
    pkg.elements.filter(_.stereotype == "EventTypeContainer").toSeq ++ pkg.packages.flatMap(allObjects)
}