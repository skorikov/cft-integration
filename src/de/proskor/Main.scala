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

class Main extends Extension with EAAdapter {
  def start(repository: Repository) {}
  def close() = EADataBase.saveFailureModes()

  def merge(repository: Repository) {
    new MergeDialog(repository)
  }

  def createDiagram(repository: Repository) {
    repository.selected foreach {
      case component: Component => {
        val pkg = component.parent.get.asInstanceOf[Package]
        val diagram = pkg.createDiagram(component.name + "_DIAGRAM")
        DiagramCreator.create(component, diagram)
        repository.show(diagram)
      }
    }
  }

  def failureModes(repository: Repository) {
    repository.selected foreach {
      case element: Element => {
        new FailureModesDialog(element, repository)
      }
      case _ =>
    }
  }

  def printId(repository: Repository) {
    repository.selected.foreach {
      case element: Element => repository.write(element.id.toString)
      case _ =>
    }
  }
}