package de.proskor
import de.proskor.ea.EAAdapter
import de.proskor.model.Element
import de.proskor.model.Entity
import de.proskor.model.Package
import de.proskor.model.Repository
import javax.swing.JOptionPane
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Shell
import de.proskor.ui.MergeDialog
import de.proskor.model.cft.Component
import de.proskor.core.DiagramCreator
import de.proskor.ui.FailureModesDialog
import de.proskor.ea.EADataBase
import cli.EA.App
import cli.EA.AppClass
import org.eclipse.swt.widgets.MessageBox
import org.eclipse.swt.SWT
import de.proskor.ea.model.EARepository
import de.proskor.model.Model
import de.proskor.model.Diagram
import de.proskor.model.Identity
import de.proskor.model.Container
import de.proskor.ea.model.EAElement

class Main extends Extension with EAAdapter {
  def start() {}
  def stop() {}
  
  def merge() {
    val repository = Repository.getCurrent
    new MergeDialog(repository)
  }

  def createDiagram() {
    val repository = Repository.getCurrent
    repository.selected foreach {
      case component: Component => {
        val pkg = component.parent.get.asInstanceOf[Package]
        val diagram = pkg.createDiagram(component.name + "_DIAGRAM")
        DiagramCreator.create(component, diagram)
        repository.show(diagram)
      }
    }
  }

  def failureModes() {
    val repository = Repository.getCurrent
    repository.selected foreach {
      case element: Element => {
        new FailureModesDialog(element, repository)
      }
      case _ =>
    }
  }

  def printId() {
    val repository = Repository.getCurrent
    repository.selected.foreach {
      case element: Element => repository.write(element.id.toString)
      case _ =>
    }
  }

  def test() {
    Repository.getCurrent.write("hello")
  }
  
}

object Main {
  def main(args: Array[String]) {
    println(cli.System.Environment.get_Version().ToString())
    /*new MergeDialog(new Repository() {
      var name: String = ""
      val id: Int = 0
      def parent: Option[Container] = None
      def kids: List[Entity] = List()
      def selected: Option[Identity] = None
  def write(text: String) {}
  def query(sql: String): String = ""
  def models: List[Model] = List()
  def show(diagram: Diagram) {}
  def get(id: Int): Element = null
    })*/
  }
}