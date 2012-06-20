package de.proskor.fel.impl

import java.util.{List => JavaList}

import scala.collection.JavaConversions.seqAsJavaList

import de.proskor.automation.Element
import de.proskor.automation.Package
import de.proskor.automation.Repository
import de.proskor.fel.container.EventTypeContainer
import de.proskor.fel.EventRepository

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