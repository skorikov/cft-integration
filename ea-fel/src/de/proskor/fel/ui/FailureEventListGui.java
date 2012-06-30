package de.proskor.fel.ui;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import de.proskor.fel.EventRepository;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.EventType;
import de.proskor.fel.ui.GuiHandlers.GuiHandlerComponents;
import de.proskor.fel.ui.GuiHandlers.GuiHandlerCreateEvent;
import de.proskor.fel.ui.GuiHandlers.GuiHandlerEvents;

public class FailureEventListGui extends Shell {
	private final FailureEventList failureEventList;
	private final EventRepository eventRepository;

	private final GuiHandlerComponents guiHandlerComponents;
	private final GuiHandlerEvents guiHandlerEvents;
	private final GuiHandlerCreateEvent guiHandlerCreateEvent;
	
	public Button btnClose;
	public Text textEventFilterByFieldsMatch;
	public Text textComponentsSelectByFieldsMatch;

	private TreeColumn trclmnComponentName;
	private TreeColumn trclmnComponentDescription;
	private TreeColumn trclmnComponentAuthor;
	private TreeColumn trclmnComponentIdGuid;
	private Tree treeComponents;
	private TreeColumn trclmnEventName;
	private TreeColumn trclmnEventComponent;
	private TreeColumn treeColumnEventDescription;
	private TreeColumn trclmnEventAuthor;
	private TreeColumn treeColumnEventIdGuid;


	private Tree treeEvents;
	private Combo comboEventFilterMode;
	private Combo comboComponentsSelectByFieldsMatch;
	private Group grpComponents;
	private Group grpEvents;
	private Text textCreateEventName;
	private Text textCreateEventAuthor;
	private Text textCreateEventComponent;
	private StyledText textCreateEventDescription;
	private Button btnChkCreateEventIsValid;
	private Button btnCreateEvent;
	private ToolItem toolItem;
	private ToolItem toolItem_1;
	private ToolItem toolItemSuperComopnents;
	private ToolItem toolItemSubComponents;
	private ToolItem tltmInvertSelection;
	private ToolItem tltmA;
	private ToolItem toolItem_2;
	private ToolItem tltmClear;
	
	/**
	 * @wbp.parser.constructor
	 */
	public FailureEventListGui(FailureEventList failureEventList) {
		super(SWT.CLOSE | SWT.MIN | SWT.TITLE);
		
		createComponents();
		
		this.failureEventList = failureEventList;
		this.eventRepository = failureEventList.getRepository();
		
		// GUI Handler:
		guiHandlerComponents = new GuiHandlerComponents(eventRepository, treeComponents, comboComponentsSelectByFieldsMatch, textComponentsSelectByFieldsMatch);
		guiHandlerCreateEvent = new GuiHandlerCreateEvent(textCreateEventName, textCreateEventAuthor, textCreateEventComponent, textCreateEventDescription, btnCreateEvent, btnChkCreateEventIsValid);
		guiHandlerEvents = new GuiHandlerEvents(treeEvents, comboComponentsSelectByFieldsMatch, textComponentsSelectByFieldsMatch);

		// Ausgangs-Zustände herstellen:
		guiHandlerComponents.loadContainerList();
		guiHandlerCreateEvent.componentsSelectionChanged(guiHandlerComponents.getSelectedComponent()); // Um aktuellen Component zu übernehmen
		guiHandlerEvents.componentsSetSelection(guiHandlerComponents.getSelectedComponents()); // Um aktuellen Component zu übernehmen
	}
	
	private void guiOpComponentsTreeChanged() {
		guiHandlerCreateEvent.componentsSelectionChanged(guiHandlerComponents.getSelectedComponent());					
		guiHandlerEvents.componentsSetSelection(guiHandlerComponents.getSelectedComponents());
	}

//	/**
//	 * Create the shell.
//	 * @param display
//	 * @wbp.parser.constructor
//	 */
//	public FailureEventListGui(GuiHandler guiListener, Display display) {
//		super(display, SWT.CLOSE | SWT.MIN | SWT.TITLE);
//		this.guiHandler = guiListener;
//
//		createComponents();
//	}

	public void show() {
		prepareToShow();

		open();
		layout();

		while (!isDisposed()) {
			if (!getDisplay().readAndDispatch()) {
				getDisplay().sleep();
			}
		}
	}

	private void prepareToShow() {

	}


	private void createComponents() {	
		this.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				// Wenn Fenster schließt.
			}
		});

		btnClose = new Button(this, SWT.CENTER);
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		btnClose.setBounds(886, 457, 98, 25);
		btnClose.setText("Close");

		grpEvents = new Group(this, SWT.NONE);
		grpEvents.setText(" Events ");
		grpEvents.setBounds(501, 10, 483, 341);

		Label lblFilterByEvent = new Label(grpEvents, SWT.NONE);
		lblFilterByEvent.setBounds(10, 21, 30, 15);
		lblFilterByEvent.setText("Filter:");

		textEventFilterByFieldsMatch = new Text(grpEvents, SWT.BORDER);
		textEventFilterByFieldsMatch.setToolTipText("Only shows events matching the filter.");
		textEventFilterByFieldsMatch.setBounds(43, 18, 314, 21);

		treeEvents = new Tree(grpEvents, SWT.BORDER | SWT.FULL_SELECTION);
		treeEvents.setLinesVisible(true);
		treeEvents.setHeaderVisible(true);
		treeEvents.setBounds(10, 50, 463, 250);

		trclmnEventName = new TreeColumn(treeEvents, SWT.LEFT);
		trclmnEventName.setWidth(150);
		trclmnEventName.setText("Event");

		trclmnEventComponent = new TreeColumn(treeEvents, SWT.CENTER);
		trclmnEventComponent.setWidth(120);
		trclmnEventComponent.setText("Component");
		
		treeColumnEventDescription = new TreeColumn(treeEvents, SWT.NONE);
		treeColumnEventDescription.setWidth(120);
		treeColumnEventDescription.setText("Description");
		
		trclmnEventAuthor = new TreeColumn(treeEvents, SWT.NONE);
		trclmnEventAuthor.setWidth(120);
		trclmnEventAuthor.setText("Author");

		treeColumnEventIdGuid = new TreeColumn(treeEvents, SWT.LEFT);
		treeColumnEventIdGuid.setWidth(150);
		treeColumnEventIdGuid.setText("ID / GUID");

		comboEventFilterMode = new Combo(grpEvents, SWT.BORDER | SWT.READ_ONLY);
		// Nur zur Vorschau:
		comboEventFilterMode.setItems(new String[] {"[[ Any Field ]]", "Name", "Author", "Description", "ID", "GUID"});
		comboEventFilterMode.setBounds(363, 17, 110, 23);
		// Tatsächliche Items werden im GUI-Handler geschrieben
		comboEventFilterMode.select(0);
		
		ToolBar toolBar_3 = new ToolBar(grpEvents, SWT.FLAT | SWT.RIGHT);
		toolBar_3.setBounds(239, 308, 234, 23);
		
		ToolItem tltmCopyAll = new ToolItem(toolBar_3, SWT.NONE);
		tltmCopyAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EventType event = guiHandlerEvents.getSelectedEvent();
				
				guiHandlerCreateEvent.componentsSelectionChanged(event.getContainer());
				guiHandlerCreateEvent.eventDataChanged();
			}
		});
		tltmCopyAll.setToolTipText("Copies all data of the currently selected Event into the \"Create Event\" dialog.");
		tltmCopyAll.setText("[All]");
		
		toolItem = new ToolItem(toolBar_3, SWT.SEPARATOR);
		
		ToolItem tltmCopyAuthor = new ToolItem(toolBar_3, SWT.NONE);
		tltmCopyAuthor.setToolTipText("Copies the author of the currently selected Event into the \"Create Event\" dialog.");
		tltmCopyAuthor.setText("Author");
		
		ToolItem tltmCopyDescription = new ToolItem(toolBar_3, SWT.NONE);
		tltmCopyDescription.setToolTipText("Copies the description of the currently selected Event into the \"Create Event\" dialog.");
		tltmCopyDescription.setText("Description");
		
		ToolItem tltmCopyComponent = new ToolItem(toolBar_3, SWT.NONE);
		tltmCopyComponent.setToolTipText("Copies component of the currently selected Event into the \"Create Event\" dialog.");
		tltmCopyComponent.setText("Component");
		
		Label lblCopy = new Label(grpEvents, SWT.NONE);
		lblCopy.setBounds(172, 313, 61, 15);
		lblCopy.setText("Reuse Data:");
		grpEvents.setTabList(new Control[]{textEventFilterByFieldsMatch, comboEventFilterMode, treeEvents});

		grpComponents = new Group(this, SWT.NONE);
		grpComponents.setText(" Components ");
		grpComponents.setBounds(10, 10, 483, 341);
		
		ToolBar toolBar_1 = new ToolBar(grpComponents, SWT.FLAT | SWT.RIGHT);
		toolBar_1.setBounds(10, 307, 463, 23);
		
		ToolItem tltmExpand = new ToolItem(toolBar_1, SWT.NONE);
		tltmExpand.setToolTipText("Expand selected Components and their children.");
		tltmExpand.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandlerComponents.componentTreeExpand(true);
			}
		});
		tltmExpand.setText("Expand");
		
		ToolItem tltmExpandAll = new ToolItem(toolBar_1, SWT.NONE);
		tltmExpandAll.setToolTipText("Expand all Components and their children.");
		tltmExpandAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandlerComponents.componentTreeExpand(false);
			}
		});
		tltmExpandAll.setText("Expand All");
		
		tltmA = new ToolItem(toolBar_1, SWT.SEPARATOR);
		
		ToolItem tltmCollapse = new ToolItem(toolBar_1, SWT.NONE);
		tltmCollapse.setToolTipText("Collapse selected Components and their children.");
		tltmCollapse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandlerComponents.componentTreeCollapse(true);
			}
		});
		tltmCollapse.setText("Collapse");
		
		ToolItem tltmCollapseAll = new ToolItem(toolBar_1, SWT.NONE);
		tltmCollapseAll.setToolTipText("Collapse all Components and their children.");
		tltmCollapseAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandlerComponents.componentTreeCollapse(false);
			}
		});
		tltmCollapseAll.setText("Collapse All");
		
		toolItem_1 = new ToolItem(toolBar_1, SWT.SEPARATOR);
		
		toolItemSuperComopnents = new ToolItem(toolBar_1, SWT.NONE);
		toolItemSuperComopnents.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandlerComponents.selectSuperComponents();
				guiOpComponentsTreeChanged();
			}
		});
		toolItemSuperComopnents.setToolTipText("Select all SuperComponents of currently selected component(s).");
		toolItemSuperComopnents.setText("Parents");
		
		toolItemSubComponents = new ToolItem(toolBar_1, SWT.NONE);
		toolItemSubComponents.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandlerComponents.selectSubComponents();
				guiOpComponentsTreeChanged();
			}
		});
		toolItemSubComponents.setToolTipText("Select all SubComponents of currently selected component(s).");
		toolItemSubComponents.setText("Childern");
		
		toolItem_2 = new ToolItem(toolBar_1, SWT.SEPARATOR);
		
		tltmInvertSelection = new ToolItem(toolBar_1, SWT.NONE);
		tltmInvertSelection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandlerComponents.selectionInvert();				
				guiOpComponentsTreeChanged();
			}
		});
		tltmInvertSelection.setToolTipText("Inverts the current selection. Selects the not-selected components and vice versa.");
		tltmInvertSelection.setText("Invert");
		
		tltmClear = new ToolItem(toolBar_1, SWT.NONE);
		tltmClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandlerComponents.clearSelection();
				guiOpComponentsTreeChanged();
			}
		});
		tltmClear.setToolTipText("Clears the selection.");
		tltmClear.setText("Clear");

		treeComponents = new Tree(grpComponents, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		treeComponents.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiOpComponentsTreeChanged();
			}
		});
		treeComponents.setLinesVisible(true);
		treeComponents.setHeaderVisible(true);
		treeComponents.setBounds(10, 50, 463, 250);

		trclmnComponentName = new TreeColumn(treeComponents, SWT.LEFT);
		trclmnComponentName.setWidth(200);
		trclmnComponentName.setText("Component");

		trclmnComponentDescription = new TreeColumn(treeComponents, SWT.CENTER);
		trclmnComponentDescription.setWidth(120);
		trclmnComponentDescription.setText("Description");

		trclmnComponentAuthor = new TreeColumn(treeComponents, SWT.LEFT);
		trclmnComponentAuthor.setWidth(120);
		trclmnComponentAuthor.setText("Author");

		trclmnComponentIdGuid = new TreeColumn(treeComponents, SWT.LEFT);
		trclmnComponentIdGuid.setWidth(150);
		trclmnComponentIdGuid.setText("ID / GUID");

		textComponentsSelectByFieldsMatch = new Text(grpComponents, SWT.BORDER);
		textComponentsSelectByFieldsMatch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (keyEventIsReturn(e)) {
					// Führt Button-OnClick aus. 
					guiHandlerComponents.selectContainerByFieldMatch(true); 
					guiOpComponentsTreeChanged();
				}
			}
		});
		textComponentsSelectByFieldsMatch.setToolTipText("Selects all components which match the specified name.");
		textComponentsSelectByFieldsMatch.setBounds(40, 18, 159, 21);

		Label lblFind = new Label(grpComponents, SWT.NONE);
		lblFind.setBounds(10, 21, 55, 15);
		lblFind.setText("Find:");
		
		comboComponentsSelectByFieldsMatch = new Combo(grpComponents, SWT.READ_ONLY);
		comboComponentsSelectByFieldsMatch.setItems(new String[] {"[[ Any Field ]]", "Name", "Author", "Description", "ID", "GUID"});
		comboComponentsSelectByFieldsMatch.setBounds(363, 17, 110, 23);
		comboComponentsSelectByFieldsMatch.select(0);
		
		ToolBar toolBar = new ToolBar(grpComponents, SWT.FLAT | SWT.RIGHT);
		toolBar.setBounds(205, 17, 152, 23);
		
		ToolItem tltmSelectionSet = new ToolItem(toolBar, SWT.NONE);
		tltmSelectionSet.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandlerComponents.selectContainerByFieldMatch(true);
				guiHandlerComponents.componentTreeExpand(true);
				guiOpComponentsTreeChanged();
			}
		});
		tltmSelectionSet.setToolTipText("Selects the specified component(s).");
		tltmSelectionSet.setText("Select");
		
		ToolItem tltmSelectionAdd = new ToolItem(toolBar, SWT.NONE);
		tltmSelectionAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandlerComponents.selectContainerByFieldMatch(false);
				guiHandlerComponents.componentTreeExpand(true);
				guiOpComponentsTreeChanged();
			}
		});
		tltmSelectionAdd.setToolTipText("Adds the matching component(s) to the selection.");
		tltmSelectionAdd.setText("Select++");
		
		ToolItem tltmSelectionRemove = new ToolItem(toolBar, SWT.NONE);
		tltmSelectionRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandlerComponents.unselectContainerByFieldMatch();
				guiOpComponentsTreeChanged();
			}
		});
		tltmSelectionRemove.setToolTipText("Removes the matching component(s) from the selection.");
		tltmSelectionRemove.setText("Select--");
		grpComponents.setTabList(new Control[]{textComponentsSelectByFieldsMatch, treeComponents});

		
		Group grpCreateEvent = new Group(this, SWT.NONE);
		grpCreateEvent.setText(" Create Event ");
		grpCreateEvent.setBounds(10, 352, 974, 100);
		
		Label lblName = new Label(grpCreateEvent, SWT.NONE);
		lblName.setText("Name:");
		lblName.setBounds(10, 25, 41, 15);
		
		textCreateEventName = new Text(grpCreateEvent, SWT.BORDER);
		textCreateEventName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// "Create Event" Klick simulieren, falls Button enabled:
				if (keyEventIsReturn(e))
					if (btnCreateEvent.isEnabled())
						btnCreateEvent.notifyListeners(SWT.Selection, new Event());
			}
		});
		textCreateEventName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				guiHandlerCreateEvent.eventDataChanged();
			}
		});
		textCreateEventName.setToolTipText("Name of the event to you wish to create. The Name hast to be unique within the containing component.");
		textCreateEventName.setBounds(83, 22, 271, 21);
		
		btnCreateEvent = new Button(grpCreateEvent, SWT.NONE);
		btnCreateEvent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandlerCreateEvent.createEvent();
				guiHandlerCreateEvent.eventDataChanged();
				
				guiHandlerEvents.reloadEvents();
			}
		});
		btnCreateEvent.setText("Create Event");
		btnCreateEvent.setBounds(854, 21, 110, 32);
		
		Label lblAuthor = new Label(grpCreateEvent, SWT.NONE);
		lblAuthor.setText("Author:");
		lblAuthor.setBounds(10, 49, 41, 15);
		
		textCreateEventAuthor = new Text(grpCreateEvent, SWT.BORDER);
		textCreateEventAuthor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// "Create Event" Klick simulieren, falls Button enabled:
				if (keyEventIsReturn(e))
					if (btnCreateEvent.isEnabled())
						btnCreateEvent.notifyListeners(SWT.Selection, new Event());
			}
		});
		textCreateEventAuthor.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				guiHandlerCreateEvent.eventDataChanged();
			}
		});
		textCreateEventAuthor.setToolTipText("Specifies the name of the author who created this event.");
		textCreateEventAuthor.setBounds(83, 46, 320, 21);
		
		Button buttonCreateEventClearData = new Button(grpCreateEvent, SWT.NONE);
		buttonCreateEventClearData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandlerCreateEvent.clearData();
			}
		});
		buttonCreateEventClearData.setText("Clear");
		buttonCreateEventClearData.setBounds(854, 58, 110, 32);
		
		Label lblComponent = new Label(grpCreateEvent, SWT.NONE);
		lblComponent.setText("Component:");
		lblComponent.setBounds(10, 73, 67, 15);
		
		textCreateEventComponent = new Text(grpCreateEvent, SWT.BORDER);
		textCreateEventComponent.setEditable(false);
		textCreateEventComponent.setToolTipText("The qualified name of the component containing the new event.");
		textCreateEventComponent.setBounds(83, 70, 320, 21);
		
		Label lblDescription = new Label(grpCreateEvent, SWT.NONE);
		lblDescription.setText("Description:");
		lblDescription.setBounds(421, 25, 65, 15);
		
		textCreateEventDescription = new StyledText(grpCreateEvent, SWT.BORDER);
		textCreateEventDescription.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				guiHandlerCreateEvent.eventDataChanged();
			}
		});
		textCreateEventDescription.setBounds(492, 22, 350, 68);
		setTabList(new Control[]{grpComponents, grpEvents, btnClose});		
		
		btnChkCreateEventIsValid = new Button(grpCreateEvent, SWT.TOGGLE);
		btnChkCreateEventIsValid.setToolTipText("The Name hast to be unique within the containing component."); // wird nicht angezeigt, wenn Button.enabled==false
		btnChkCreateEventIsValid.setBounds(360, 21, 43, 22);
		btnChkCreateEventIsValid.setText("valid");
		btnChkCreateEventIsValid.setEnabled(false);
		grpCreateEvent.setTabList(new Control[]{textCreateEventName, textCreateEventAuthor, textCreateEventDescription, btnCreateEvent, buttonCreateEventClearData});

		createContents();
	}

	private boolean keyEventIsReturn(KeyEvent e) {
		return e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR; // SWT.CR == RETURN; SWT.KEYPAD_CR == NUM_RETURN
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Failure Event List");
		setSize(1000, 520);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
