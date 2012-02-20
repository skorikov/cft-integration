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