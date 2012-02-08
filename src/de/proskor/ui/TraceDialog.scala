package de.proskor.ui
import org.eclipse.swt.events.DisposeEvent
import org.eclipse.swt.events.DisposeListener
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.widgets.Tree
import org.eclipse.swt.widgets.TreeItem
import org.eclipse.swt.SWT
import de.proskor.core.MergeTrace
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.widgets.Text
import org.eclipse.swt.widgets.Label
import de.proskor.model.cft.Component
import de.proskor.model.cft.Gate
import de.proskor.model.cft.Input
import de.proskor.model.cft.Output
import de.proskor.model.cft.Event
import org.eclipse.swt.graphics.Font

class TraceDialog(display: Display, trace: MergeTrace) {
  val componentIcon = new Image(display, this.getClass().getClassLoader().getResourceAsStream("icons/component.gif"))
  val gateIcon = new Image(display, this.getClass().getClassLoader().getResourceAsStream("icons/gate.gif"))
  val inputIcon = new Image(display, this.getClass().getClassLoader().getResourceAsStream("icons/input.gif"))
  val outputIcon = new Image(display, this.getClass().getClassLoader().getResourceAsStream("icons/output.gif"))
  val eventIcon = new Image(display, this.getClass().getClassLoader().getResourceAsStream("icons/event.gif"))
  val mergeIcon = new Image(display, this.getClass().getClassLoader().getResourceAsStream("icons/merge.png"))
  val conflictColor = Display.getCurrent.getSystemColor(SWT.COLOR_RED)
  val mergeColor = Display.getCurrent.getSystemColor(SWT.COLOR_BLUE)
  val systemFontData = Display.getCurrent.getSystemFont.getFontData()(0)
  systemFontData.setStyle(SWT.BOLD)
  val bold = new Font(display, systemFontData)
  val monospaced = new Font(display, "Courier", systemFontData.getHeight, SWT.NONE)

  val shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.RESIZE);
  shell.setText("Merge Trace");
  shell.setImage(mergeIcon)
  shell.addDisposeListener(new DisposeListener() {
    def widgetDisposed(event: DisposeEvent) {
      componentIcon.dispose()
      gateIcon.dispose()
      inputIcon.dispose()
      outputIcon.dispose()
      eventIcon.dispose()
      mergeIcon.dispose()
    }
  })
  shell.setLayout(new GridLayout(2, false))
  val tree = new Tree(shell, SWT.BORDER | SWT.V_SCROLL)
  populate(tree, trace)
  val treeData = new GridData
  treeData.verticalAlignment = GridData.FILL;
  treeData.horizontalAlignment = GridData.FILL;
  treeData.widthHint = 200;
  treeData.grabExcessVerticalSpace = true;
  tree.setLayoutData(treeData);

  val details = new Composite(shell, SWT.NONE)
  val detailsLayout = new GridLayout(2, false)
  detailsLayout.marginWidth = 0
  detailsLayout.marginHeight = 0
  details.setLayout(detailsLayout)
  val detailsData = new GridData(SWT.FILL, SWT.FILL, true, true)
  details.setLayoutData(detailsData)

  val closeButton = new Button(shell, SWT.PUSH)
  closeButton.setText("Close")
  val closeButtonData = new GridData(SWT.RIGHT, SWT.CENTER, false, false)
  closeButtonData.horizontalSpan = 2
  closeButton.setLayoutData(closeButtonData)
  closeButton.addSelectionListener(new SelectionAdapter() {
    override def widgetSelected(e: SelectionEvent) {
      shell.close()
    }
  })

  val resultLabel = new Label(details, SWT.NONE)
  val resultLabelData = new GridData(SWT.FILL, SWT.CENTER, true, false)
  resultLabelData.horizontalSpan = 2
  resultLabel.setLayoutData(resultLabelData)
  val resultText = new Text(details, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL)
  resultText.setFont(monospaced)
  val resultTextData = new GridData(SWT.FILL, SWT.FILL, true, true)
  resultTextData.horizontalSpan = 2
  resultText.setLayoutData(resultTextData)

  val leftLabel = new Label(details, SWT.NONE)
  val leftLabelData = new GridData(SWT.FILL, SWT.CENTER, true, false)
  leftLabel.setLayoutData(leftLabelData)
  val rightLabel = new Label(details, SWT.NONE)
  val rightLabelData = new GridData(SWT.FILL, SWT.CENTER, true, false)
  rightLabel.setLayoutData(rightLabelData)

  val leftText = new Text(details, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL)
  val leftTextData = new GridData(SWT.FILL, SWT.FILL, true, true)
  leftText.setFont(monospaced)
  leftText.setLayoutData(leftTextData)
  val rightText = new Text(details, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL)
  rightText.setFont(monospaced)
  val rightTextData = new GridData(SWT.FILL, SWT.FILL, true, true)
  rightText.setLayoutData(rightTextData)

  tree.addSelectionListener(new SelectionAdapter {
    override def widgetSelected(e: SelectionEvent) {
      val treeItem = e.item.asInstanceOf[TreeItem]
      val traceElement = treeItem.getData("DATA").asInstanceOf[MergeTrace]
      resultLabel.setText(traceElement.element.fullName)
      resultText.setText(traceElement.element.toString)
      traceElement.sources.size match {
        case 1 => {
          leftLabel.setText(traceElement.sources(0).fullName)
          leftText.setText(traceElement.sources(0).toString)
          rightLabel.setText("--")
          rightText.setText("--")
        }
        case 2 => {
          leftLabel.setText(traceElement.sources(0).fullName)
          leftText.setText(traceElement.sources(0).toString)
          rightLabel.setText(traceElement.sources(1).fullName)
          rightText.setText(traceElement.sources(1).toString)
        }
      }
      details.layout()
    }
  })

  shell.setSize(800, 600)
  shell.open()

  private def populate(tree: Tree, trace: MergeTrace) {
    val treeItem = new TreeItem(tree, SWT.NONE)
    if (trace.sources.size > 1) {
      treeItem.setFont(bold)
      if (trace.hasConflicts) treeItem.setForeground(conflictColor)
      else treeItem.setForeground(mergeColor)
    }
    treeItem.setImage(trace.element match {
      case component: Component => componentIcon
      case gate: Gate => gateIcon
      case input: Input => inputIcon
      case output: Output => outputIcon
      case event: Event => eventIcon
    })
    treeItem.setText(trace.element.elementName)
    treeItem.setData("DATA", trace)
    for (kid <- trace.kids) {
      populateKids(treeItem, kid)
    }
  }

  private def populateKids(tree: TreeItem, trace: MergeTrace) {
    val treeItem = new TreeItem(tree, SWT.NONE)
    if (trace.sources.size > 1) {
      treeItem.setFont(bold)
      if (trace.hasConflicts) treeItem.setForeground(conflictColor)
      else treeItem.setForeground(mergeColor)
    }
    treeItem.setImage(trace.element match {
      case component: Component => componentIcon
      case gate: Gate => gateIcon
      case input: Input => inputIcon
      case output: Output => outputIcon
      case event: Event => eventIcon
    })
    treeItem.setText(trace.element.elementName)
    treeItem.setData("DATA", trace)
    for (kid <- trace.kids) {
      populateKids(treeItem, kid)
    }
  }
}