package de.proskor.shell

import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.FormLayout
import org.eclipse.swt.widgets.Text
import org.eclipse.swt.layout.FormData
import org.eclipse.swt.layout.FormAttachment
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.epsilon.eol.IEolModule
import org.eclipse.epsilon.eol.EolModule
import de.proskor.emc.automation.AutomationModel
import de.proskor.automation.Repository
import java.io.PrintStream
import java.io.OutputStream
import org.eclipse.swt.graphics.Font

class EpsilonShell {
  val display = new Display
  val shell = createShell(display)
  shell.open()
  while (!shell.isDisposed) {
    if (!display.readAndDispatch())
      display.sleep()
  }
  display.dispose()

  private def createShell(display: Display): Shell = {
    val shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.RESIZE | SWT.ON_TOP)
    shell setText "Epsilon Shell"
    shell setLayout new FormLayout

    val text = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL)
    text setFont new Font(display, "Courier New", 8, SWT.NORMAL)
    text setTabs 4
    text setText """printEventTypes();

operation printEventTypes() {
  var fel := getFEL();
  fel.printElements();
}

operation getFEL(): Package {
  var repository = Repository.allInstances().first();
  var mod := repository.models.first();
  return mod.packages.selectOne(pkg | pkg.name == "FEL");
}

operation Package printElements() {
  for (element in self.elements) {
    element.name.println();
  }
}"""

    val execute = new Button(shell, SWT.PUSH)
    execute setText "E&xecute"
    execute addSelectionListener new SelectionAdapter {
      override def widgetSelected(event: SelectionEvent) {
        val repository = Repository.instance
        val module: IEolModule = new EolModule
        val ok = module.parse(text.getText)
        if (ok) {
          val model = new AutomationModel(Set(repository))
          model.setName("AUTOMATION")
          module.getContext.getModelRepository.addModel(model)
          val output: OutputStream = new OutputStream {
            private val buffer = new StringBuffer
            override def write(char: Int) = buffer append char.asInstanceOf[Char]
            override def toString: String = buffer toString
          }
          val printStream = new PrintStream(output)
          module.getContext.setOutputStream(printStream)
          try {
            module.execute
          } catch {
            case e: Exception => e.printStackTrace(printStream)
          }
          output.toString.split("\n").map(_.trim).map(repository.write)
        } else {
          repository.write("syntax error")
        }
      } 
    }

    var data = new FormData(600, 400)
    data.left = new FormAttachment(0, 5)
    data.top = new FormAttachment(0, 5)
    data.right = new FormAttachment(100, -5)
    data.bottom = new FormAttachment(execute, -5, SWT.TOP)
    text setLayoutData data

    data = new FormData
    data.bottom = new FormAttachment(100, -5)
    data.right = new FormAttachment(100, -5)
    execute setLayoutData data

    shell.pack

    shell
  }
}