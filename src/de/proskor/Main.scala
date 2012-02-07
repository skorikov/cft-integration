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

class Main extends Extension with EAAdapter {
  def close() {}

  def merge(repository: Repository) {
    new MergeDialog(repository)
  }
}