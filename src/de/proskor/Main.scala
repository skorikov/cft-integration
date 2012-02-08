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

class Main extends Extension with EAAdapter {
  def close() {}

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
}