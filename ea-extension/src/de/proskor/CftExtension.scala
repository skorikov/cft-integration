package de.proskor

import de.proskor.extension.MenuItemAdapter
import de.proskor.fel.EventRepository
import de.proskor.fel.impl.ArchitecturalViewImpl
import de.proskor.fel.impl.ComponentFaultTreeImpl
import de.proskor.fel.impl.EventRepositoryImpl
import de.proskor.fel.ui.FailureEventList
import de.proskor.fel.ui.FailureEventListFactory
import de.proskor.fel.view.ArchitecturalView
import de.proskor.model.Diagram
import de.proskor.model.ModelTests
import de.proskor.shell.EpsilonShell

class CftExtension extends ExtensionWithTest {
  private lazy val tab = this.getRepository.getOutputTab("TESTS")
  private lazy val runner = new TestRunner(this.tab.write)
  private val menu = new MenuItemAdapter("CFT")

  private def item(name: String)(code: => Unit) =
    new MenuItemAdapter(menu, name) {
      override def run = code
    }

  override protected def createMenu = {
    val repository = this.getRepository
    new MenuItemAdapter(menu, "Run Tests") {
//      setEnabled(false)
      override def run {
//        AutomationTests.repository = CftExtension.this.getRepository
        tab.write("---- RUNNING TESTS ----")
        runner.test(classOf[ModelTests])
//        runner.test(classOf[AutomationTests])
//        runner.test(classOf[AdapterTests])
//        runner.test(classOf[PeerTests])
//        runner.test(classOf[CftTests])
        tab.write("---- ALL TESTS DONE ----")
      }
    }

    /*item("Failure Event List...") {
      val er: EventRepository = new EventRepositoryImpl(Repository.instance)
      val dialog: FailureEventList = new FailureEventListImpl(er)
      dialog.showDialog
    }*/

    item("Failure Event List...") {
      val er: EventRepository = new EventRepositoryImpl(this.getRepository())
      val dialog: FailureEventList = FailureEventListFactory.createGUI(er)
      dialog.showDialog
    }

    item("Epsilon Shell...") {
      new EpsilonShell
    }

  /*  item("Test") {
      val repository = Repository.instance
      val vr: ViewRepository = new EventRepositoryImpl(repository)
      for (view <- vr.getViews) view match {
        case cft: ComponentFaultTree => {
          repository.write("CFT: " + cft.getName)
          repository.write("--CONTEXT: " + cft.getContext.getName)
        }
        case av: ArchitecturalView => {
          repository.write("AV: " + av.getName)
          for (etc <- av.getEventTypeContainers) {
            repository.write("--ETC: " + etc.getName)
          }
        }
      }
    }*/

    new MenuItemAdapter(menu, "Derive CFT") {
      override def isVisible = repository.getContext match {
        case diagram: Diagram if diagram.getStereotype != "CFT" => true
        case _ => false
      }
      override def run {
      //  val repository = Repository.instance
      //  val context = repository.context.get.asInstanceOf[Diagram]
        val context = repository.getContext.asInstanceOf[Diagram]
        val cft = context.getPackage.getDiagrams.add(context.getName + ".CFT", Diagram.OBJECT)
        cft.setStereotype("CFT")
        val av: ArchitecturalView = new ArchitecturalViewImpl(repository, context)
        new ComponentFaultTreeImpl(repository, cft).setContext(av)
        cft.open()
      }
    }

  /*  new MenuItemAdapter(menu, "Available Components") {
      override def isVisible = Repository.instance.diagram match {
        case Some(diagram: Diagram) if diagram.stereotype == "CFT" => true
        case _ => false
      }
      override def isEnabled = hasChildren
      override def getChildren: JavaList[MenuItem] = {
        val repository = Repository.instance
        val vr: ViewRepository = new EventRepositoryImpl(repository)
        if (repository.diagram.isEmpty) return List()
        val diagram = repository.diagram.get
        val cft = new ComponentFaultTreeImpl(diagram)
        if (cft.getContext == null) return List()
        for (container <- cft.getContext.getEventTypeContainers)
          yield new MenuItemAdapter(container.getName) {
            override def run {
              val pkg = diagram.pkg
              val element = pkg.elements.add(container.getName, "Object")
              element.stereotype = "Component"
              val connector = element.connectors.add("", "Connector")
              connector.source = element
              connector.target = container.asInstanceOf[EventTypeContainerImpl].peer
              connector.stereotype = "instanceOf"
              val node = diagram.nodes.add(container.getName, "Object")
              node.element = element
              node.left = 10
              node.top = 10
              node.width = 300
              node.height = 200
            }
          }
      }
    }*/

  /*  def findNode(diagram: Diagram, element: Element): Option[Node] = diagram.nodes.find(_.element == element)

    def createNode(diagram: Diagram, parent: Node, kid: Element) {
      val node = diagram.nodes.add("", "Object")
      node.element = kid
      node.left = parent.left + (parent.width - 40) / 2
      node.top = parent.top + (parent.height - 40) / 2
      node.width = 40
      node.height = 40
      node.sequence = parent.sequence - 1
    }

    def isConnected(element: Element): Boolean = element.connectors.exists(_.stereotype == "instanceOf")*/

  /*  new MenuItemAdapter(menu, "Assign Component Type") {
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
          override def run {
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
    }*/

  /*  new MenuItemAdapter(menu, "Create Event Instance") {
      override def isVisible = hasChildren

      override def getChildren: JavaList[MenuItem] = for (event <- eventTypes) yield new MenuItemAdapter(event.getName) {
        override def run {
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
    }*/

    menu
  }
}