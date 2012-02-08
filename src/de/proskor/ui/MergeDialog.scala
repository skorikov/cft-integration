package de.proskor.ui
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.layout.RowLayout
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.widgets.Combo
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Label
import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.SWT
import de.proskor.model.Repository
import de.proskor.model.cft.Source
import de.proskor.model.cft.Component
import de.proskor.model.Package
import de.proskor.model.cft.Or
import de.proskor.model.cft.And
import de.proskor.model.cft.Gate
import de.proskor.model.Element
import de.proskor.model.cft.Output
import de.proskor.model.Entity
import org.eclipse.swt.events.DisposeListener
import org.eclipse.swt.events.DisposeEvent
import org.eclipse.swt.widgets.Control
import de.proskor.core.MergeAlgorithm
import de.proskor.core.MergeTrace
import de.proskor.core.DiagramCreator

class MergeDialog(repository: Repository) {
  val display = new Display
  val shell = createShell(display)
  shell.open()
  while (!shell.isDisposed) {
    if (!display.readAndDispatch())
      display.sleep()
  }
  display.dispose()

  private def createShell(display: Display): Shell = {
    val shell = new Shell(display, SWT.CLOSE | SWT.TITLE)
    val icon = new Image(display, this.getClass().getClassLoader().getResourceAsStream("icons/merge.png"))
    shell.setImage(icon)
    shell.setText("CFT Integration")
    shell.addDisposeListener(new DisposeListener() {
      def widgetDisposed(event: DisposeEvent) {
        icon.dispose()
      }
    })
    buildGui(shell)
    shell
  }

  private def buildGui(shell: Shell) {
    val components = repository.allComponents
    val packages = repository.allPackages

	val mainLayout = new GridLayout
	mainLayout.numColumns = 2
	shell.setLayout(mainLayout)
	
	val mergeLabel = new Label(shell, SWT.NONE)
	mergeLabel.setText("Merge")
	mergeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false))
	
	val left = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY)
	left.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false))
	fillCombo(left, components)
	
	val wit = new Label(shell, SWT.None)
	wit.setText("with")
	wit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false))
	
	val right = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY)
	right.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false))
	fillCombo(right, components)
	
	val storeIn = new Label(shell, SWT.NONE)
	storeIn.setText("into")
	storeIn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false))
	
	val target = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY)
	target.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false))
	fillCombo(target, packages)
	
	val buttons = new Composite(shell, SWT.NONE)
	val gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false)
	gridData.horizontalSpan = 2
	buttons.setLayoutData(gridData)
	val buttonsLayout = new RowLayout
	buttons.setLayout(buttonsLayout)
	
	val ok = new Button(buttons, SWT.PUSH)
	ok.setText("Merge")
	ok.addSelectionListener(new SelectionAdapter() {
	  override def widgetSelected(e: SelectionEvent) {
	    merge(components(left.getSelectionIndex), components(right.getSelectionIndex), packages(target.getSelectionIndex))
	  }
	});
	
	val cancel = new Button(buttons, SWT.PUSH)
	cancel.setText("Cancel")
	cancel.addSelectionListener(new SelectionAdapter() {
	  override def widgetSelected(e: SelectionEvent) {
	    shell.close()
	  }
	})

    shell.pack()
  }

  private def merge(left: Component, right: Component, target: Package) {
    disableAllKids(shell)
    shell.update()
    MergeAlgorithm.conflicts = List()
    MergeAlgorithm.repository = repository
    val mergeThread = new Thread {
      override def run {
        val map = MergeAlgorithm.merge(left, right, target)
        val trace = map._1 ++ map._2
        val diagram = target.createDiagram(left.name + "_DIAGRAM")
        DiagramCreator.create(MergeAlgorithm.mergeResult, diagram)
        repository.show(diagram)
        printTrace(MergeAlgorithm.mergeResult, trace)
        finished
      }
      private def finished {
        display.asyncExec(new Runnable {
          def run {
            enableAllKids(shell)
          }
        })
      }
    }
    mergeThread.run()
  }

  private def printTrace(component: Component, trace: Map[Element, Element]) {
    val mergeTrace = MergeTrace.calculate(component, reverseTrace(trace), MergeAlgorithm.conflicts)
    new TraceDialog(display, mergeTrace)
  }

  private def reverseTrace(trace: Map[Element, Element]): Map[Element, List[Element]] = {
    val result = scala.collection.mutable.Map[Element, List[Element]]()
    for ((source, target) <- trace) {
      result.get(target) match {
        case None => result += (target -> List(source))
        case Some(sources) => result += (target -> (sources ::: List(source)))
      }
    }
    result.toMap
  }

  private def fillCombo(combo: Combo, elements: Seq[Entity]) {
    combo.setItems(elements.map(_.fullName).toArray)
    combo.select(0)
  }

  private def forAll[T](control: Control, f: Control => T): Unit = control match {
    case composite: Composite => f(composite); composite.getChildren.foreach(forAll(_, f))
    case leaf: Control => f(leaf)
  }
  private def setEnabledForAllKids(shell: Shell, enabled: Boolean) = shell.getChildren.foreach(forAll(_, _.setEnabled(enabled)))
  private def enableAllKids(shell: Shell) = setEnabledForAllKids(shell, true)
  private def disableAllKids(shell: Shell) = setEnabledForAllKids(shell, false)

}