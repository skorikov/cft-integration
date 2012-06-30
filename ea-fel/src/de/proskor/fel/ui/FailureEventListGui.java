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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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
import de.proskor.fel.ui.GuiHandlers.GuiHandlerComponents;
import de.proskor.fel.ui.GuiHandlers.GuiHandlerCreateEvent;
import de.proskor.fel.ui.GuiHandlers.GuiHandlerEvents;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

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
	private ToolBar toolBar_2;
	private ToolItem tltmSupercomponents;
	private ToolItem tltmSubcomponents;
	
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
		guiHandlerEvents = new GuiHandlerEvents(treeComponents, comboComponentsSelectByFieldsMatch, textComponentsSelectByFieldsMatch);

		// Ausgangs-Zustände herstellen:
		guiHandlerComponents.updateGui();
		guiHandlerEvents.updateGui();
		
		guiHandlerCreateEvent.componentsSelectionChanged(guiHandlerComponents.getSelectedComponents()); // Um aktuellen Component zu übernehmen
		guiHandlerCreateEvent.updateGui();
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
		
		Button btnShowSupercomponentEvents = new Button(grpEvents, SWT.CHECK);
		btnShowSupercomponentEvents.setToolTipText("Displays events contained in super-components, if there are any.");
		btnShowSupercomponentEvents.setBounds(10, 312, 196, 16);
		btnShowSupercomponentEvents.setText("include super-component events");
		
		Button btnDisplaySubcomponentEvents = new Button(grpEvents, SWT.CHECK);
		btnDisplaySubcomponentEvents.setToolTipText("Displays events contained in sub-components, if there are any.");
		btnDisplaySubcomponentEvents.setText("include sub-component events");
		btnDisplaySubcomponentEvents.setBounds(288, 312, 185, 16);
		grpEvents.setTabList(new Control[]{textEventFilterByFieldsMatch, comboEventFilterMode, treeEvents, btnShowSupercomponentEvents, btnDisplaySubcomponentEvents});

		grpComponents = new Group(this, SWT.NONE);
		grpComponents.setText(" Components ");
		grpComponents.setBounds(10, 10, 483, 341);
		
		ToolBar toolBar_1 = new ToolBar(grpComponents, SWT.FLAT | SWT.RIGHT);
		toolBar_1.setBounds(10, 307, 244, 23);
		
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

		treeComponents = new Tree(grpComponents, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		treeComponents.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<EventTypeContainer> selection = guiHandlerComponents.getSelectedComponents();
				guiHandlerCreateEvent.componentsSelectionChanged(selection);
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
				if (keyEventIsReturn(e)) 
					guiHandlerComponents.selectContainerByFieldMatch(true); // Führt Button-OnClick aus. Kann man bei SWT nicht direkt über Button.onClick() machen.  -.- 
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
		
		ToolItem tltmSelect = new ToolItem(toolBar, SWT.NONE);
		tltmSelect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandlerComponents.selectContainerByFieldMatch(true);
			}
		});
		tltmSelect.setToolTipText("Selects the specified component(s).");
		tltmSelect.setText("Select");
		
		ToolItem tltmSelect_1 = new ToolItem(toolBar, SWT.NONE);
		tltmSelect_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandlerComponents.selectContainerByFieldMatch(false);
			}
		});
		tltmSelect_1.setToolTipText("Adds the matching component(s) to the selection.");
		tltmSelect_1.setText("Select++");
		
		ToolItem tltmSelect_2 = new ToolItem(toolBar, SWT.NONE);
		tltmSelect_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandlerComponents.unselectContainerByFieldMatch();
			}
		});
		tltmSelect_2.setToolTipText("Removes the matching component(s) from the selection.");
		tltmSelect_2.setText("Select--");
		
		toolBar_2 = new ToolBar(grpComponents, SWT.FLAT | SWT.RIGHT);
		toolBar_2.setBounds(263, 307, 210, 23);
		
		tltmSupercomponents = new ToolItem(toolBar_2, SWT.NONE);
		tltmSupercomponents.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandlerComponents.selectSuperComponents();
			}
		});
		tltmSupercomponents.setToolTipText("Select all SuperComponents of currently selected component(s).");
		tltmSupercomponents.setText("SuperComponents");
		
		tltmSubcomponents = new ToolItem(toolBar_2, SWT.NONE);
		tltmSubcomponents.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandlerComponents.selectSubComponents();
			}
		});
		tltmSubcomponents.setToolTipText("Select all SubComponents of currently selected component(s).");
		tltmSubcomponents.setText("SubComponents");
		grpComponents.setTabList(new Control[]{textComponentsSelectByFieldsMatch, treeComponents});

		
		Group grpCreateEvent = new Group(this, SWT.NONE);
		grpCreateEvent.setText(" Create Event ");
		grpCreateEvent.setBounds(10, 352, 974, 100);
		
		Label lblName = new Label(grpCreateEvent, SWT.NONE);
		lblName.setText("Name:");
		lblName.setBounds(10, 25, 41, 15);
		
		textCreateEventName = new Text(grpCreateEvent, SWT.BORDER);
		textCreateEventName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				guiHandlerCreateEvent.updateGui();
			}
		});
		textCreateEventName.setToolTipText("Name of the event to you wish to create. The Name hast to be unique within the containing component.");
		textCreateEventName.setBounds(83, 22, 271, 21);
		
		btnCreateEvent = new Button(grpCreateEvent, SWT.NONE);
		btnCreateEvent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandlerCreateEvent.createEvent();
			}
		});
		btnCreateEvent.setText("Create Event");
		btnCreateEvent.setBounds(854, 21, 110, 32);
		
		Label lblAuthor = new Label(grpCreateEvent, SWT.NONE);
		lblAuthor.setText("Author:");
		lblAuthor.setBounds(10, 49, 41, 15);
		
		textCreateEventAuthor = new Text(grpCreateEvent, SWT.BORDER);
		textCreateEventAuthor.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				guiHandlerCreateEvent.updateGui();
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
				guiHandlerCreateEvent.updateGui();
			}
		});
		textCreateEventDescription.setBounds(492, 22, 350, 68);
		setTabList(new Control[]{grpComponents, grpEvents, btnClose});		
		
		btnChkCreateEventIsValid = new Button(grpCreateEvent, SWT.TOGGLE);
		btnChkCreateEventIsValid.setToolTipText("The Name hast to be unique within the containing component."); // wird nicht angezeigt, wenn Button.enabled==false
		btnChkCreateEventIsValid.setBounds(360, 21, 43, 22);
		btnChkCreateEventIsValid.setText("valid");
		btnChkCreateEventIsValid.setEnabled(false);

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
