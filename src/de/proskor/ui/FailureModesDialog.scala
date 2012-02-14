package de.proskor.ui
import org.eclipse.swt.events.DisposeEvent
import org.eclipse.swt.events.DisposeListener
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.SWT

import de.proskor.ea.model.fel.EABasicFailureMode
import de.proskor.model.Element
import de.proskor.model.Repository

class FailureModesDialog(element: Element, repository: Repository) {
//  val display = new Display
//  val shell = createShell(display)
//  shell.open()
//  while (!shell.isDisposed) {
//    if (!display.readAndDispatch())
//      display.sleep()
//  }
//  display.dispose()

  val newFM = new EABasicFailureMode(element, "test", "desc", "bob")
  for (failureMode <- element.allFailureModes) {
    repository.write(failureMode.toString)
  }

  private def createShell(display: Display): Shell = {
    val shell = new Shell(display, SWT.CLOSE | SWT.TITLE)
    val icon = new Image(display, this.getClass().getClassLoader().getResourceAsStream("icons/merge.png"))
    shell.setImage(icon)
    shell.setText("Failure Modes")
    shell.addDisposeListener(new DisposeListener() {
      def widgetDisposed(event: DisposeEvent) {
        icon.dispose()
      }
    })
    buildGui(shell)
    shell
  }

  private def buildGui(shell: Shell) {}
}