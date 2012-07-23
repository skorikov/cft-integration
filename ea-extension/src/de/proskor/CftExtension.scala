package de.proskor

import java.util.Collections
import java.util.{List => JavaList}
import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.seqAsJavaList
import de.proskor.automation.Diagram
import de.proskor.automation.Element
import de.proskor.automation.Node
import de.proskor.automation.Repository
import de.proskor.cft.test.AutomationTests
import de.proskor.cft.test.AdapterTests
import de.proskor.cft.test.CftTests
import de.proskor.cft.test.PeerTests
import de.proskor.extension.ExtensionAdapter
import de.proskor.extension.MenuItem
import de.proskor.extension.MenuItemAdapter
import de.proskor.fel.container.EventInstanceContainer
import de.proskor.fel.event.EventType
import de.proskor.fel.impl.EventInstanceContainerImpl
import de.proskor.fel.impl.EventRepositoryImpl
import de.proskor.fel.impl.EventTypeContainerImpl
import de.proskor.fel.impl.EventTypeImpl
import de.proskor.fel.ui.FailureEventListImpl
import de.proskor.fel.EventRepository
import de.proskor.shell.EpsilonShell
import de.proskor.fel.ui.FailureEventList
import de.proskor.fel.view.ViewRepository
import de.proskor.fel.view.ArchitecturalView
import de.proskor.fel.view.ComponentFaultTree
import de.proskor.fel.impl.ComponentFaultTreeImpl
import de.proskor.fel.impl.ArchitecturalViewImpl
import de.proskor.model.{Element => JavaElement, Package => JavaPackage}
import de.proskor.model.impl.DiagramImpl

class CftExtension extends ExtensionAdapter {
  private val runner = new TestRunner(Repository.instance.write)
  private val menu = new MenuItemAdapter("CFT")

  private def item(name: String)(code: => Unit) =
    new MenuItemAdapter(menu, name) {
      override def run = code
    }

  override def deleteElement(element: JavaElement): Boolean = {
    this.getRepository().getOutputTab("delete").write("deleting " + element.getName())
    !element.getName().equals("test")
  }

  override def deletePackage(pkg: JavaPackage): Boolean = {
    this.getRepository().getOutputTab("delete").write("deleting " + pkg.getName())
    !pkg.getName().equals("test")
  }

  override protected def createMenu = {
    new MenuItemAdapter(menu, "Run Tests") {
//      setVisible(false)
      override def run {
        AutomationTests.repository = CftExtension.this.getRepository
        Repository.instance.write("---- RUNNING TESTS ----")
        runner.test(classOf[AutomationTests])
//        runner.test(classOf[AdapterTests])
//        runner.test(classOf[PeerTests])
//        runner.test(classOf[CftTests])
        Repository.instance.write("---- ALL TESTS DONE ----")
      }
    }

    item("Failure Event List...") {
      val er: EventRepository = new EventRepositoryImpl(Repository.instance)
      val dialog: FailureEventList = new FailureEventListImpl(er)
      dialog.showDialog
    }

    item("Epsilon Shell...") {
      new EpsilonShell
    }

    item("Write") {
      val repository = this.getRepository();
      val output = repository.getOutputTab("TEST");
      val models = repository.getModels();
      val iterator = models.iterator();
      while (iterator.hasNext()) {
        val model = iterator.next();
        var it = model.getPackages().iterator();
        while (it.hasNext()) {
          val pkg = it.next();
          output.write(pkg.getName());
          val di = pkg.getDiagrams().iterator();
          while (di.hasNext()) {
            val diagram = di.next();
            output.write(diagram.getName());
            val ni = diagram.getNodes().iterator();
            while (ni.hasNext()) {
              val node = ni.next();
              output.write("[" + node.getId() + "] " + node.getElement().getName() + " (" + node.getLeft() + "," + node.getTop() + "," + node.getRight() + "," + node.getBottom() + "|" + node.getSequence() + ")");
            }
          }
        }
      }
    }

    item("CONTEXT") {
      val repository = this.getRepository();
      val output = repository.getOutputTab("Context");
      val context = repository.getContext();
      output.write(if (context != null) context.toString() else "NULL");
      if (context.isInstanceOf[de.proskor.model.Package]) {
        val element = context.asInstanceOf[de.proskor.model.Package].getElement();
        val iterator = element.getTaggedValues().iterator();
        while (iterator.hasNext()) {
          val tv = iterator.next();
          output.write(tv.toString());
        }
      }
    }

    item("FOO") {
      val repository = this.getRepository();
      val output = repository.getOutputTab("TEST");
      val models = repository.getModels();
      val iterator = models.iterator();
      while (iterator.hasNext()) {
        val model = iterator.next();
        val it = model.getPackages().iterator();
        while (it.hasNext()) {
          val pkg = it.next();
          val pi = pkg.getElements().iterator();
          while (pi.hasNext()) {
            val element = pi.next();
            output.write(element.getName());
            val ci = element.getConnectors().iterator();
            while (ci.hasNext()) {
              val connector = ci.next();
              output.write(connector.getSource().getName() + " -> " + connector.getTarget().getName());
            }
          }
        }
      }
    }

    item("Test") {
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
    }

    new MenuItemAdapter(menu, "Derive CFT") {
      override def isVisible = Repository.instance.context match {
        case Some(diagram: Diagram) if diagram.stereotype != "CFT" => true
        case _ => false
      }
      override def run {
        val repository = Repository.instance
        val context = repository.context.get.asInstanceOf[Diagram]
        val cft = context.pkg.diagrams.add(context.name + ".CFT", "ObjectDiagram")
        cft.stereotype = "CFT"
        val av = new ArchitecturalViewImpl(context)
        new ComponentFaultTreeImpl(cft).setContext(av)
        cft.open()
      }
    }

    new MenuItemAdapter(menu, "Available Components") {
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
          yield new MenuItemAdapter(container.getName)
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
    }

    new MenuItemAdapter(menu, "Create Event Instance") {
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
    }

    menu
  }
}