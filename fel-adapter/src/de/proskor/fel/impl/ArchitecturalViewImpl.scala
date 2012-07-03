package de.proskor.fel.impl

import java.util.{List => JavaList}
import scala.collection.JavaConversions._
import de.proskor.automation.Diagram
import de.proskor.fel.container.EventTypeContainer
import de.proskor.fel.view.ArchitecturalView

class ArchitecturalViewImpl(diagram: Diagram) extends ViewImpl(diagram) with ArchitecturalView {
  override def getEventTypeContainers: JavaList[EventTypeContainer] = {
    val list = for {
      node <- diagram.nodes
      element = node.element
      if element.stereotype == "EventTypeContainer"
    } yield new EventTypeContainerImpl(element)
    list.toList
  }
}