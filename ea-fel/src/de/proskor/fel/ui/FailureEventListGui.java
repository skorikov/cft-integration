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
import de.proskor.fel.ui.GuiHandlers.GuiHandlerCreateEvent.EventData;
import de.proskor.fel.ui.GuiHandlers.GuiHandlerEvents;

public class FailureEventListGui extends Shell {
	private final FailureEventList failureEventList;
	private final EventRepository eventRepository;

	private final GuiHandlerComponents guiHandlerComponents;
	private final GuiHandlerEvents guiHandlerEvents;
	private final GuiHandlerCreateEvent guiHandlerCreateEvent;
	private StatusManager statusManager;
	
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
	private ToolItem tltmCopyName;
	private ToolItem tltmComponent;
	
	private Label lblStatusComponentsCount;
	private Label lblStatusEventsVisibleCount;
	private Label lblComponentsSelectedCount;
	private Button btnComponentsReload;

	
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
		guiHandlerEvents = new GuiHandlerEvents(treeEvents, comboEventFilterMode, textEventFilterByFieldsMatch);
		
		statusManager = new StatusManager(lblStatusComponentsCount, lblComponentsSelectedCount, lblStatusEventsVisibleCount);
		
		// Ausgangs-Zustände herstellen:
		guiHandlerComponents.loadContainerList();
		guiHandlerComponents.selectFirstTypeInTree(); // Ersten Component markieren
		guiOpComponentsTreeChanged();
	}

	/**
	 * Impliziert {@link #guiOpEventsTreeChanged()}
	 */
	private void guiOpComponentsTreeChanged() {
		List<EventTypeContainer> selectedComponents = guiHandlerComponents.getSelectedComponents();
		
		guiHandlerCreateEvent.componentsSelectionChanged(guiHandlerComponents.getSelectedComponent());					
		
		statusManager.setComponentsSelectedCount(selectedComponents.size());		
		statusManager.setComponentsCount(guiHandlerComponents.getComponents().size()); // eigentlich nur einmal nötig - aber falls components nachgeladen werden (z.B. durch eine weitere Funktion "Reload"), muss diese Zahl aktualisiert werden.
	
		guiOpEventsTreeChanged();
	}

	private void guiOpEventsTreeChanged() {
		List<EventTypeContainer> selectedComponents = guiHandlerComponents.getSelectedComponents();
		guiHandlerEvents.componentsSetSelection(selectedComponents);

		statusManager.setEventsDisplayedCount(guiHandlerEvents.getEvents().size());		
	}

	private static class StatusManager {
		private Label componentsCount;
		private Label componentsSelectedCount;
		private Label eventsDisplayedCount;
		
		public StatusManager(Label componentsCount, Label componentsSelectedCount, Label eventsDisplayedCount) {
			this.componentsCount = componentsCount;
			this.componentsSelectedCount = componentsSelectedCount;
			this.eventsDisplayedCount = eventsDisplayedCount;
		}
		
		public void setComponentsCount(int count) {
			componentsCount.setText("Components: " + count);
		}
		
		public void setComponentsSelectedCount(int count) {
			componentsSelectedCount.setText("Components selected: " + count);
		}
		
		public void setEventsDisplayedCount(int count) {
			eventsDisplayedCount.setText("Events displayed: " + count);
		}
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
		btnClose.setBounds(884, 457, 100, 25);
		btnClose.setText("Close");

		grpEvents = new Group(this, SWT.NONE);
		grpEvents.setText(" Events ");
		grpEvents.setBounds(501, 10, 483, 341);

		Label lblFilterByEvent = new Label(grpEvents, SWT.NONE);
		lblFilterByEvent.setBounds(10, 21, 30, 15);
		lblFilterByEvent.setText("Filter:");

		textEventFilterByFieldsMatch = new Text(grpEvents, SWT.BORDER);
		textEventFilterByFieldsMatch.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				guiHandlerEvents.reloadEvents();
				guiOpEventsTreeChanged();
			}
		});
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
		comboEventFilterMode.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (guiHandlerEvents != null) {// dies ist beim Initialisieren der GUI der Fall. Danach nicht mehr.
					guiHandlerEvents.reloadEvents();
					guiOpEventsTreeChanged();
				}
			}
		});
		// Nur zur Vorschau:
		comboEventFilterMode.setItems(new String[] {"[[ Any Field ]]", "Name", "Author", "Description", "ID", "GUID"});
		comboEventFilterMode.setBounds(363, 17, 110, 23);
		// Tatsächliche Items werden im GUI-Handler geschrieben
		comboEventFilterMode.select(0);
		
		ToolBar toolBar_3 = new ToolBar(grpEvents, SWT.FLAT | SWT.RIGHT);
		toolBar_3.setBounds(192, 308, 281, 23);
		
		ToolItem tltmCopyAll = new ToolItem(toolBar_3, SWT.NONE);
		tltmCopyAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EventType event = guiHandlerEvents.getSelectedEvent();
				
				EventData eventData = new EventData();
				eventData.container = event.getContainer();
				eventData.name = event.getName();
				eventData.author = event.getAuthor(); 
				eventData.description = event.getDescription();
						
				guiHandlerCreateEvent.setEventData(eventData);
				guiHandlerCreateEvent.eventDataChanged();
			}
		});
		tltmCopyAll.setToolTipText("Copies all data of the currently selected Event into the \"Create Event\" dialog.");
		tltmCopyAll.setText("[All]");
		
		toolItem = new ToolItem(toolBar_3, SWT.SEPARATOR);
		
		tltmCopyName = new ToolItem(toolBar_3, SWT.NONE);
		tltmCopyName.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EventType event = guiHandlerEvents.getSelectedEvent();
				
				EventData eventData = new EventData();
				eventData.name = event.getName();
						
				guiHandlerCreateEvent.setEventData(eventData);
				guiHandlerCreateEvent.eventDataChanged();
			}
		});
		tltmCopyName.setText("Name");
		
		ToolItem tltmCopyAuthor = new ToolItem(toolBar_3, SWT.NONE);
		tltmCopyAuthor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EventType event = guiHandlerEvents.getSelectedEvent();
				
				EventData eventData = new EventData();
				eventData.author = event.getAuthor(); 
						
				guiHandlerCreateEvent.setEventData(eventData);
				guiHandlerCreateEvent.eventDataChanged();
			}
		});
		tltmCopyAuthor.setToolTipText("Copies the author of the currently selected Event into the \"Create Event\" dialog.");
		tltmCopyAuthor.setText("Author");
		
		ToolItem tltmCopyDescription = new ToolItem(toolBar_3, SWT.NONE);
		tltmCopyDescription.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EventType event = guiHandlerEvents.getSelectedEvent();
				
				EventData eventData = new EventData();
				eventData.description = event.getDescription();
						
				guiHandlerCreateEvent.setEventData(eventData);
				guiHandlerCreateEvent.eventDataChanged();
			}
		});
		tltmCopyDescription.setToolTipText("Copies the description of the currently selected Event into the \"Create Event\" dialog.");
		tltmCopyDescription.setText("Description");
		
		tltmComponent = new ToolItem(toolBar_3, SWT.NONE);
		tltmComponent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EventType event = guiHandlerEvents.getSelectedEvent();
				
				EventData eventData = new EventData();
				eventData.container = event.getContainer();
						
				guiHandlerCreateEvent.setEventData(eventData);
				guiHandlerCreateEvent.eventDataChanged();
			}
		});
		tltmComponent.setText("Component");
		
		ToolItem tltmCopyComponent = new ToolItem(toolBar_3, SWT.NONE);
		tltmCopyComponent.setToolTipText("Copies component of the currently selected Event into the \"Create Event\" dialog.");
		tltmCopyComponent.setText("Component");
		
		Label lblCopy = new Label(grpEvents, SWT.NONE);
		lblCopy.setBounds(125, 313, 61, 15);
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
				guiOpEventsTreeChanged();
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

		Group group = new Group(this, SWT.NONE);
		group.setBounds(10, 450, 483, 30);
		
		lblStatusComponentsCount = new Label(group, SWT.NONE);
		lblStatusComponentsCount.setText("Components: 10");
		lblStatusComponentsCount.setBounds(10, 10, 134, 15);
		
		lblStatusEventsVisibleCount = new Label(group, SWT.NONE);
		lblStatusEventsVisibleCount.setText("Events displayed: 10");
		lblStatusEventsVisibleCount.setBounds(343, 10, 130, 15);
		
		lblComponentsSelectedCount = new Label(group, SWT.NONE);
		lblComponentsSelectedCount.setText("Components selected: 10");
		lblComponentsSelectedCount.setBounds(150, 10, 150, 15);
		
		btnComponentsReload = new Button(this, SWT.NONE);
		btnComponentsReload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandlerComponents.loadContainerList();
				guiOpComponentsTreeChanged();
			}
		});
		btnComponentsReload.setToolTipText("Reloads components from Enterprise Architect incase there have been changes.");
		btnComponentsReload.setBounds(778, 457, 100, 25);
		btnComponentsReload.setText("Reload");

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
