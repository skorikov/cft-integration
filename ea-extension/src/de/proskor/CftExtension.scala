package de.proskor

import java.util.Collections
import java.util.{List => JavaList}

import scala.collection.JavaConversions._

import de.proskor.automation.Diagram
import de.proskor.automation.Element
import de.proskor.automation.Node
import de.proskor.automation.Repository
import de.proskor.cft.test.AdapterTests
import de.proskor.cft.test.CftTests
import de.proskor.cft.test.PeerTests
import de.proskor.extension.ExtensionAdapter
import de.proskor.extension.MenuItem
import de.proskor.extension.MenuItemAdapter
import de.proskor.fel.container.EventInstanceContainer
import de.proskor.fel.container.EventTypeContainer
import de.proskor.fel.event.EventType
import de.proskor.fel.impl.EventInstanceContainerImpl
import de.proskor.fel.impl.EventRepositoryImpl
import de.proskor.fel.impl.EventTypeContainerImpl
import de.proskor.fel.impl.EventTypeImpl
import de.proskor.fel.ui.FailureEventListDialog
import de.proskor.fel.ui.FailureEventListImpl
import de.proskor.fel.EventRepository
import de.proskor.shell.EpsilonShell

class CftExtension extends ExtensionAdapter {
  private val runner = new TestRunner(Repository.instance.write)
  private val menu = new MenuItemAdapter("CFT")
  private var selected: Option[Element] = None

  private def item(name: String)(code: => Unit) =
    new MenuItemAdapter(menu, name) {
      override def invoke = code
    }

  override protected def createMenu = {
    new MenuItemAdapter(menu, "Run Tests") {
      setVisible(false)
      override def invoke {
        Repository.instance.write("---- RUNNING TESTS ----")
        runner.test(classOf[AdapterTests])
        runner.test(classOf[PeerTests])
        runner.test(classOf[CftTests])
        Repository.instance.write("---- ALL TESTS DONE ----")
      }
    }

    item("Failure Event List...") {
      val er: EventRepository = new EventRepositoryImpl(Repository.instance)
      val dialog: FailureEventListDialog = new FailureEventListImpl(er)
      dialog.showEventList
    }

    item("Epsilon Shell...") {
      new EpsilonShell
    }

    item("Test") {
      Repository.instance.context match {
        case Some(el: Element) => {
          for (tv <- el.taggedValues) {
            Repository.instance.write(tv.name + ": " + tv.value + " (" + tv.description + ")")
          }
        }
        case _ =>
      }
    }

    def findNode(diagram: Diagram, element: Element): Option[Node] = diagram.nodes.find(_.element == element)

    def createNode(diagram: Diagram, parent: Node, kid: Element) {
      val node = diagram.nodes.add("", "Object")
      node.element = kid
      node.left = parent.left + (parent.width - 40) / 2
      node.top = parent.top + (parent.height - 40) / 2
      node.width = 40
      node.height = 40
      node.sequence = parent.sequence - 1
    }

    def isConnected(element: Element): Boolean = element.connectors.exists(_.stereotype == "instanceOf")

    new MenuItemAdapter(menu, "Assign Component Type") {
      override def isVisible = hasChildren

      override def hasChildren = {
        val repository = Repository.instance
        repository.context match {
          case Some(element: Element) if element.stereotype == "Component" => getChildren.nonEmpty
          case _ => false
        }
      }

      override def getChildren: JavaList[MenuItem] = {

        val repository = Repository.instance
        val er: EventRepository = new EventRepositoryImpl(repository)
        val el: Element = repository.context.get.asInstanceOf[Element]
        var typeContainer: EventTypeContainerImpl = null
        if (isConnected(el)) {
          val container = new EventInstanceContainerImpl(el)
          typeContainer = container.getType.asInstanceOf[EventTypeContainerImpl]
        }
        for (cont <- er.getEventTypeContainers) yield new MenuItemAdapter(cont.getName) {
          override def isChecked = isConnected(el) && (cont.asInstanceOf[EventTypeContainerImpl].peer == typeContainer.peer)
          override def invoke {
            if (!isChecked) {
              el.connectors.find(_.stereotype == "instanceOf") match {
                case Some(connector) => connector.target = cont.asInstanceOf[EventTypeContainerImpl].peer
                case None => {
                  val connector = el.connectors.add("", "Connector")
                  connector.source = el
                  connector.target = cont.asInstanceOf[EventTypeContainerImpl].peer
                  connector.stereotype = "instanceOf"
                }
              }
            }
          }
        }
      }
    }

    new MenuItemAdapter(menu, "Create Event Instance") {
      override def isVisible = hasChildren

      override def getChildren: JavaList[MenuItem] = for (event <- eventTypes) yield new MenuItemAdapter(event.getName) {
        override def invoke {
          val el = repository.context.get.asInstanceOf[Element]
          val diagram = repository.diagram
          var node: Option[Node] = None
          if (diagram.isDefined)
            node = findNode(diagram.get, el)

          val eventInstance = el.elements.add(selectedContainer.get.getType.getName + "." + event.getName, "Object")
          eventInstance.stereotype = "Event"
          val c2 = eventInstance.connectors.add("", "Connector")
          c2.source = eventInstance
          c2.target = event.asInstanceOf[EventTypeImpl].peer
          c2.stereotype = "instanceOf"

          if (node.nonEmpty)
            createNode(diagram.get, node.get, eventInstance)
        }
      }

      private def repository = Repository.instance

      private def selectedContainer: Option[EventInstanceContainer] = repository.context collect {
        case element: Element if element.stereotype == "Component" && isConnected(element) => new EventInstanceContainerImpl(element)
      }

      private def eventTypes: JavaList[EventType] = selectedContainer map { _.getType.getEvents } getOrElse Collections.emptyList()
    }

    menu
  }
}