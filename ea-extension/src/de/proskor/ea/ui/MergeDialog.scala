package de.proskor.ea.ui
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
import de.proskor.cft.model._
import org.eclipse.swt.events.DisposeListener
import org.eclipse.swt.events.DisposeEvent
import org.eclipse.swt.widgets.Control
import de.proskor.cft.merge.MergeAlgorithm
import de.proskor.cft.merge.MergeTrace
import de.proskor.ea.ui.RichElement._

class RichElement(val self: Element) {
  def fullName: String = self.parent match {
    case Some(container) => container.fullName + "/" + self.name
    case None => self.name
  }
}

object RichElement {
  implicit def toRichElement(element: Element): RichElement = new RichElement(element)
}

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
  //  val icon = new Image(display, this.getClass().getClassLoader().getResourceAsStream("icons/merge.png"))
  //  shell.setImage(icon)
    shell.setText("CFT Integration")
    shell.addDisposeListener(new DisposeListener() {
      def widgetDisposed(event: DisposeEvent) {
      //  icon.dispose()
      }
    })
    buildGui(shell)
    shell
  }

  private def allComponents(pkg: Package): Set[Component] = pkg.components ++ pkg.packages.flatMap(allComponents)
  private def allPackages(pkg: Package): Set[Package] = pkg.packages ++ pkg.packages.flatMap(allPackages)

  private def buildGui(shell: Shell) {
    val components = allComponents(repository).toSeq.sortBy(_.fullName)
    val packages = allPackages(repository).toSeq.sortBy(_.fullName)

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
    val al = new MergeAlgorithm
    val trace = new MergeTrace
    val mergeThread = new Thread {
      override def run {
        al.merge(left, right, target, trace)
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


  private def fillCombo(combo: Combo, elements: Seq[Element]) {
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