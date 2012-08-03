package de.proskor.fel.ui.impl;

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
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
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
import de.proskor.fel.ui.FailureEventList;
import de.proskor.fel.ui.impl.GuiHandlers.GuiHandlerComponents;
import de.proskor.fel.ui.impl.GuiHandlers.GuiHandlerCreateEvent;
import de.proskor.fel.ui.impl.GuiHandlers.GuiHandlerEvents;
import de.proskor.fel.ui.impl.GuiHandlers.GuiHandlerCreateEvent.EventData;

class FailureEventListGui extends Shell {
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
	private Group grpInfos;
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
	private Group grpCreateEvent;
	
	/**
	 * @wbp.parser.constructor
	 */
	public FailureEventListGui(FailureEventList failureEventList) {
		super(SWT.SHELL_TRIM);
		
		createComponents();
		
		this.failureEventList = failureEventList;
		this.eventRepository = failureEventList.getRepository();
		
		// GUI Handler:
		guiHandlerComponents = new GuiHandlerComponents(eventRepository, treeComponents, comboComponentsSelectByFieldsMatch, textComponentsSelectByFieldsMatch);
		guiHandlerCreateEvent = new GuiHandlerCreateEvent(textCreateEventName, textCreateEventAuthor, textCreateEventComponent, textCreateEventDescription, btnCreateEvent, btnChkCreateEventIsValid);
		guiHandlerEvents = new GuiHandlerEvents(treeEvents, comboEventFilterMode, textEventFilterByFieldsMatch);
		
		statusManager = new StatusManager(lblStatusComponentsCount, lblComponentsSelectedCount, lblStatusEventsVisibleCount);
		
		// Ausgangs-Zust�nde herstellen:
		guiHandlerComponents.loadContainerList();
		guiHandlerComponents.selectFirstTypeInTree(); // Ersten Component markieren
		guiOpComponentsTreeChanged();
		
		guiHandlerEvents.selectFirstTypeInTree(); // Erstes Event markieren
		guiOpEventsTreeChanged();
	}
	
	public List<EventType> getEventsCreatedByUser() {
		return guiHandlerCreateEvent.getEventsCreatedByUser();
	}

	/**
	 * Impliziert {@link #guiOpEventsTreeChanged()}
	 */
	private void guiOpComponentsTreeChanged() {
		List<EventTypeContainer> selectedComponents = guiHandlerComponents.getSelectedComponents();
		
		guiHandlerCreateEvent.componentsSelectionChanged(guiHandlerComponents.getSelectedComponent());					
		
		statusManager.setComponentsSelectedCount(selectedComponents.size());		
		statusManager.setComponentsCount(guiHandlerComponents.getComponents().size()); // eigentlich nur einmal n�tig - aber falls components nachgeladen werden (z.B. durch eine weitere Funktion "Reload"), muss diese Zahl aktualisiert werden.
	
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

	public void show(EventTypeContainer selectedContainer) {
		guiHandlerComponents.selectComponent(selectedContainer);
		guiOpComponentsTreeChanged();
		
		show();
	}
	
	public void show(EventType selectedEvent) {
		// Component ausw�hlen und �nderung im GUI+Handler �bernehmen
		guiHandlerComponents.selectComponent(selectedEvent.getContainer());
		guiOpComponentsTreeChanged();
		
		// Event ausw�hlen und �nderung im GUI+Handler �bernehmen
		guiHandlerEvents.selectEvent(selectedEvent);
		guiOpEventsTreeChanged();
		
		show();
	}
	
	public void show() {
		open();
		layout();

		while (!isDisposed()) {
			if (!getDisplay().readAndDispatch()) {
				getDisplay().sleep();
			}
		}
	}


	private void createWidgetsEventsGroup() {
		grpEvents = new Group(this, SWT.NONE);
		grpEvents.setText(" Events ");
		grpEvents.setLayout(new FormLayout());

		Label lblFilterByEvent = new Label(grpEvents, SWT.NONE);
		FormData fd_lblFilterByEvent = new FormData();
		fd_lblFilterByEvent.right = new FormAttachment(0, 37);
		fd_lblFilterByEvent.top = new FormAttachment(0, 6);
		fd_lblFilterByEvent.left = new FormAttachment(0, 7);
		lblFilterByEvent.setLayoutData(fd_lblFilterByEvent);
		lblFilterByEvent.setText("Filter:");

		textEventFilterByFieldsMatch = new Text(grpEvents, SWT.BORDER);
		FormData fd_textEventFilterByFieldsMatch = new FormData();
		fd_textEventFilterByFieldsMatch.right = new FormAttachment(100, -125);
		fd_textEventFilterByFieldsMatch.top = new FormAttachment(0, 3);
		fd_textEventFilterByFieldsMatch.left = new FormAttachment(0, 40);
		textEventFilterByFieldsMatch.setLayoutData(fd_textEventFilterByFieldsMatch);
		textEventFilterByFieldsMatch.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				guiHandlerEvents.reloadEvents();
				guiOpEventsTreeChanged();
			}
		});
		textEventFilterByFieldsMatch.setToolTipText("Only shows events matching the filter.");

		comboEventFilterMode = new Combo(grpEvents, SWT.BORDER | SWT.READ_ONLY);
		FormData fd_comboEventFilterMode = new FormData();
		fd_comboEventFilterMode.left = new FormAttachment(textEventFilterByFieldsMatch, 3, SWT.RIGHT);
		fd_comboEventFilterMode.right = new FormAttachment(100, -7);
		fd_comboEventFilterMode.top = new FormAttachment(0, 2);
		comboEventFilterMode.setLayoutData(fd_comboEventFilterMode);
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
		// Tats�chliche Items werden im GUI-Handler geschrieben
		comboEventFilterMode.select(0);
		
				Label lblReuseData = new Label(grpEvents, SWT.NONE);
		FormData fd_lblCopy = new FormData();
		fd_lblCopy.left = new FormAttachment(0, 6);
		fd_lblCopy.bottom = new FormAttachment(100, -5);
		lblReuseData.setLayoutData(fd_lblCopy);
		lblReuseData.setText("Reuse Data:");
				
		ToolBar toolBar_3 = new ToolBar(grpEvents, SWT.FLAT | SWT.RIGHT);
		FormData fd_toolBar_3 = new FormData();
//		fd_toolBar_3.left = new FormAttachment(0, 60);
		fd_toolBar_3.left = new FormAttachment(lblReuseData, 0, SWT.RIGHT);
		fd_toolBar_3.bottom = new FormAttachment(100, -2);
		toolBar_3.setLayoutData(fd_toolBar_3);
		
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

		
		treeEvents = new Tree(grpEvents, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_treeEvents = new FormData();
		fd_treeEvents.right = new FormAttachment(100, -7);
		fd_treeEvents.bottom = new FormAttachment(toolBar_3, -5, SWT.TOP);
		fd_treeEvents.top = new FormAttachment(0, 35);
		fd_treeEvents.left = new FormAttachment(0, 7);
		treeEvents.setLayoutData(fd_treeEvents);
		treeEvents.setLinesVisible(true);
		treeEvents.setHeaderVisible(true);

		trclmnEventName = new TreeColumn(treeEvents, SWT.LEFT);
		trclmnEventName.setWidth(150);
		trclmnEventName.setText("Event");

		trclmnEventComponent = new TreeColumn(treeEvents, SWT.LEFT);
		trclmnEventComponent.setWidth(120);
		trclmnEventComponent.setText("Component");
		
		treeColumnEventDescription = new TreeColumn(treeEvents, SWT.LEFT);
		treeColumnEventDescription.setWidth(120);
		treeColumnEventDescription.setText("Description");
		
		trclmnEventAuthor = new TreeColumn(treeEvents, SWT.LEFT);
		trclmnEventAuthor.setWidth(120);
		trclmnEventAuthor.setText("Author");

		treeColumnEventIdGuid = new TreeColumn(treeEvents, SWT.LEFT);
		treeColumnEventIdGuid.setWidth(150);
		treeColumnEventIdGuid.setText("ID / GUID");

		grpEvents.setTabList(new Control[]{textEventFilterByFieldsMatch, comboEventFilterMode, treeEvents});
	}

	private void createWidgetsComponentsGroup() {
		grpComponents = new Group(this, SWT.NONE);
		grpComponents.setText(" Components ");
		grpComponents.setLayout(new FormLayout());

		ToolBar toolBar_1 = new ToolBar(grpComponents, SWT.FLAT | SWT.RIGHT);
		FormData fd_toolBar_1 = new FormData();
		fd_toolBar_1.left = new FormAttachment(0, 1);
		fd_toolBar_1.bottom = new FormAttachment(100, -2);
		toolBar_1.setLayoutData(fd_toolBar_1);
		
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
		FormData fd_treeComponents = new FormData();
		fd_treeComponents.top = new FormAttachment(0, 35);
		fd_treeComponents.bottom = new FormAttachment(toolBar_1, -5, SWT.TOP);
		fd_treeComponents.right = new FormAttachment(0, 470);
		fd_treeComponents.left = new FormAttachment(0, 7);
		treeComponents.setLayoutData(fd_treeComponents);
		treeComponents.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiOpComponentsTreeChanged();
			}
		});
		treeComponents.setLinesVisible(true);
		treeComponents.setHeaderVisible(true);

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
		FormData fd_textComponentsSelectByFieldsMatch = new FormData();
		fd_textComponentsSelectByFieldsMatch.right = new FormAttachment(0, 196);
		fd_textComponentsSelectByFieldsMatch.top = new FormAttachment(0, 3);
		fd_textComponentsSelectByFieldsMatch.left = new FormAttachment(0, 37);
		textComponentsSelectByFieldsMatch.setLayoutData(fd_textComponentsSelectByFieldsMatch);
		textComponentsSelectByFieldsMatch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (keyEventIsReturn(e)) {
					// F�hrt Button-OnClick aus. 
					guiHandlerComponents.selectContainerByFieldMatch(true); 
					guiOpComponentsTreeChanged();
				}
			}
		});
		textComponentsSelectByFieldsMatch.setToolTipText("Selects all components which match the specified name.");

		Label lblFind = new Label(grpComponents, SWT.NONE);
		FormData fd_lblFind = new FormData();
		fd_lblFind.right = new FormAttachment(0, 62);
		fd_lblFind.top = new FormAttachment(0, 6);
		fd_lblFind.left = new FormAttachment(0, 7);
		lblFind.setLayoutData(fd_lblFind);
		lblFind.setText("Find:");
		
		comboComponentsSelectByFieldsMatch = new Combo(grpComponents, SWT.READ_ONLY);
		FormData fd_comboComponentsSelectByFieldsMatch = new FormData();
		fd_comboComponentsSelectByFieldsMatch.right = new FormAttachment(0, 470);
		fd_comboComponentsSelectByFieldsMatch.top = new FormAttachment(0, 2);
		fd_comboComponentsSelectByFieldsMatch.left = new FormAttachment(0, 360);
		comboComponentsSelectByFieldsMatch.setLayoutData(fd_comboComponentsSelectByFieldsMatch);
		comboComponentsSelectByFieldsMatch.setItems(new String[] {"[[ Any Field ]]", "Name", "Author", "Description", "ID", "GUID"});
		comboComponentsSelectByFieldsMatch.select(0);
		
		ToolBar toolBar = new ToolBar(grpComponents, SWT.FLAT | SWT.RIGHT);
		FormData fd_toolBar = new FormData();
		fd_toolBar.top = new FormAttachment(0, 2);
		fd_toolBar.left = new FormAttachment(0, 202);
		toolBar.setLayoutData(fd_toolBar);
		
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
	}
	
	private void createWidgetsCreateEventGroup() {
		grpCreateEvent = new Group(this, SWT.NONE);
		grpCreateEvent.setText(" Create Event ");
		grpCreateEvent.setLayout(new FormLayout());

		Label lblName = new Label(grpCreateEvent, SWT.NONE);
		FormData fd_lblName = new FormData();
		fd_lblName.right = new FormAttachment(0, 48);
		fd_lblName.top = new FormAttachment(0, 10);
		fd_lblName.left = new FormAttachment(0, 7);
		lblName.setLayoutData(fd_lblName);
		lblName.setText("Name:");
		
		textCreateEventName = new Text(grpCreateEvent, SWT.BORDER);
		FormData fd_textCreateEventName = new FormData();
		fd_textCreateEventName.right = new FormAttachment(0, 351);
		fd_textCreateEventName.top = new FormAttachment(0, 7);
		fd_textCreateEventName.left = new FormAttachment(0, 80);
		textCreateEventName.setLayoutData(fd_textCreateEventName);
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
		
		btnCreateEvent = new Button(grpCreateEvent, SWT.NONE);
		FormData fd_btnCreateEvent = new FormData();
		fd_btnCreateEvent.left = new FormAttachment(100, -120);
		fd_btnCreateEvent.bottom = new FormAttachment(100, -45);
		fd_btnCreateEvent.right = new FormAttachment(100, -5);
		fd_btnCreateEvent.top = new FormAttachment(0, 7);
		btnCreateEvent.setLayoutData(fd_btnCreateEvent);
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
		
		Label lblAuthor = new Label(grpCreateEvent, SWT.NONE);
		FormData fd_lblAuthor = new FormData();
		fd_lblAuthor.right = new FormAttachment(0, 48);
		fd_lblAuthor.top = new FormAttachment(0, 34);
		fd_lblAuthor.left = new FormAttachment(0, 7);
		lblAuthor.setLayoutData(fd_lblAuthor);
		lblAuthor.setText("Author:");
		
		textCreateEventAuthor = new Text(grpCreateEvent, SWT.BORDER);
		FormData fd_textCreateEventAuthor = new FormData();
		fd_textCreateEventAuthor.right = new FormAttachment(0, 400);
		fd_textCreateEventAuthor.top = new FormAttachment(0, 31);
		fd_textCreateEventAuthor.left = new FormAttachment(0, 80);
		textCreateEventAuthor.setLayoutData(fd_textCreateEventAuthor);
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
		
		Button buttonCreateEventClearData = new Button(grpCreateEvent, SWT.NONE);
		FormData fdButtonsClearAndCreate = new FormData();
		fdButtonsClearAndCreate.left = new FormAttachment(100, -120);
		fdButtonsClearAndCreate.bottom = new FormAttachment(0, 75);
		fdButtonsClearAndCreate.right = new FormAttachment(100, -5);
		fdButtonsClearAndCreate.top = new FormAttachment(0, 43);
		buttonCreateEventClearData.setLayoutData(fdButtonsClearAndCreate);
		buttonCreateEventClearData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandlerCreateEvent.clearData();
			}
		});
		buttonCreateEventClearData.setText("Clear");
		
		Label lblComponent = new Label(grpCreateEvent, SWT.NONE);
		FormData fd_lblComponent = new FormData();
		fd_lblComponent.top = new FormAttachment(0, 58);
		fd_lblComponent.left = new FormAttachment(0, 7);
		lblComponent.setLayoutData(fd_lblComponent);
		lblComponent.setText("Component:");
		
		textCreateEventComponent = new Text(grpCreateEvent, SWT.BORDER);
		FormData fd_textCreateEventComponent = new FormData();
		fd_textCreateEventComponent.right = new FormAttachment(0, 400);
		fd_textCreateEventComponent.top = new FormAttachment(0, 55);
		fd_textCreateEventComponent.left = new FormAttachment(0, 80);
		textCreateEventComponent.setLayoutData(fd_textCreateEventComponent);
		textCreateEventComponent.setEditable(false);
		textCreateEventComponent.setToolTipText("The qualified name of the component containing the new event.");
		
		Label lblDescription = new Label(grpCreateEvent, SWT.NONE);
		FormData fd_lblDescription = new FormData();
		fd_lblDescription.right = new FormAttachment(0, 483);
		fd_lblDescription.top = new FormAttachment(0, 10);
		fd_lblDescription.left = new FormAttachment(0, 418);
		lblDescription.setLayoutData(fd_lblDescription);
		lblDescription.setText("Description:");
		
		textCreateEventDescription = new StyledText(grpCreateEvent, SWT.BORDER | SWT.WRAP);
		FormData fd_textCreateEventDescription = new FormData();
		fd_textCreateEventDescription.left = new FormAttachment(0, 489);
		fd_textCreateEventDescription.right = new FormAttachment(btnCreateEvent, -10, SWT.LEFT);
		fd_textCreateEventDescription.bottom = new FormAttachment(0, 75);
		fd_textCreateEventDescription.top = new FormAttachment(0, 7);
		textCreateEventDescription.setLayoutData(fd_textCreateEventDescription);
		textCreateEventDescription.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				guiHandlerCreateEvent.eventDataChanged();
			}
		});
		
		btnChkCreateEventIsValid = new Button(grpCreateEvent, SWT.TOGGLE);
		FormData fd_btnChkCreateEventIsValid = new FormData();
		fd_btnChkCreateEventIsValid.bottom = new FormAttachment(0, 28);
		fd_btnChkCreateEventIsValid.right = new FormAttachment(0, 400);
		fd_btnChkCreateEventIsValid.top = new FormAttachment(0, 6);
		fd_btnChkCreateEventIsValid.left = new FormAttachment(0, 357);
		btnChkCreateEventIsValid.setLayoutData(fd_btnChkCreateEventIsValid);
		btnChkCreateEventIsValid.setToolTipText("The Name hast to be unique within the containing component.");
		btnChkCreateEventIsValid.setText("valid");
		btnChkCreateEventIsValid.setEnabled(false);
		grpCreateEvent.setTabList(new Control[]{textCreateEventName, textCreateEventAuthor, textCreateEventDescription, btnCreateEvent, buttonCreateEventClearData});
	}
	
	private void createWidgetsInfosGroup() {
		grpInfos = new Group(this, SWT.NONE);
		
		lblStatusComponentsCount = new Label(grpInfos, SWT.NONE);
		lblStatusComponentsCount.setText("Components: 10");
		lblStatusComponentsCount.setBounds(10, 10, 134, 15);
		
		lblStatusEventsVisibleCount = new Label(grpInfos, SWT.NONE);
		lblStatusEventsVisibleCount.setText("Events displayed: 10");
		lblStatusEventsVisibleCount.setBounds(343, 10, 130, 15);
		
		lblComponentsSelectedCount = new Label(grpInfos, SWT.NONE);
		lblComponentsSelectedCount.setText("Components selected: 10");
		lblComponentsSelectedCount.setBounds(150, 10, 150, 15);
	}
	
	private void createWidgetsButtons() {
		btnComponentsReload = new Button(this, SWT.NONE);
		btnComponentsReload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandlerComponents.loadContainerList();
				guiOpComponentsTreeChanged();
			}
		});
		btnComponentsReload.setToolTipText("Reloads components from Enterprise Architect incase there have been changes.");
		btnComponentsReload.setText("Reload");
		
		btnClose = new Button(this, SWT.NONE);
		btnClose.setText("Close");
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});

	}
	
	private void createWidgetAlignmentsAndSizes() {
		/* Setzt die Alginments der Widgets.
		 * ==> Die Widget-Gr��en sind dadurch impliziert.
		 */
		
		// ------------------------------------------------------------------------------------------
		// Components:
		// ------------------------------------------------------------------------------------------
		FormData fd_grpComponents = new FormData();
		fd_grpComponents.bottom = new FormAttachment(grpCreateEvent, -5, SWT.TOP);
		fd_grpComponents.right = new FormAttachment(0, 493);
		fd_grpComponents.top = new FormAttachment(0, 10);
		fd_grpComponents.left = new FormAttachment(0, 10);
		grpComponents.setLayoutData(fd_grpComponents);

		
		// ------------------------------------------------------------------------------------------
		// Events:
		// ------------------------------------------------------------------------------------------
		FormData fd_grpEvents = new FormData();
		fd_grpEvents.top = new FormAttachment(0, 10);
		fd_grpEvents.bottom = new FormAttachment(grpCreateEvent, -5, SWT.TOP);
		fd_grpEvents.left = new FormAttachment(grpComponents, 10);
		fd_grpEvents.right = new FormAttachment(100, -10);
		grpEvents.setLayoutData(fd_grpEvents);

		
		// ------------------------------------------------------------------------------------------
		// Create Event:
		// ------------------------------------------------------------------------------------------
		FormData fd_grpCreateEvent = new FormData();
		fd_grpCreateEvent.top = new FormAttachment(btnClose, -110, SWT.TOP);
		fd_grpCreateEvent.bottom = new FormAttachment(btnClose, -10, SWT.TOP);
		fd_grpCreateEvent.right = new FormAttachment(100, -10);
		fd_grpCreateEvent.left = new FormAttachment(0, 10);
		grpCreateEvent.setLayoutData(fd_grpCreateEvent);

		
		// ------------------------------------------------------------------------------------------
		// Infos:
		// ------------------------------------------------------------------------------------------
		FormData fd_group = new FormData();
		fd_group.bottom = new FormAttachment(100, -8);
		fd_group.right = new FormAttachment(0, 490);
		fd_group.left = new FormAttachment(0, 10);
		grpInfos.setLayoutData(fd_group);

		
		// ------------------------------------------------------------------------------------------
		// Buttons:
		// ------------------------------------------------------------------------------------------
		// Button "Close":
		FormData fd_btnClose = new FormData();
		fd_btnClose.left = new FormAttachment(100, -120);
		fd_btnClose.right = new FormAttachment(100, -10);
		fd_btnClose.bottom = new FormAttachment(100, -5);
		btnClose.setLayoutData(fd_btnClose);

		// Button "Reload":
		FormData fd_btnComponentsReload = new FormData();
		fd_btnComponentsReload.left = new FormAttachment(btnClose, -120, SWT.LEFT);
		fd_btnComponentsReload.top = new FormAttachment(btnClose, 0, SWT.TOP);
		fd_btnComponentsReload.right = new FormAttachment(btnClose, -10);
		fd_btnComponentsReload.bottom = new FormAttachment(100, -5);
		btnComponentsReload.setLayoutData(fd_btnComponentsReload);
	}
	
	private void createComponents() {	
		this.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				// Wenn Fenster schlie�t.
			}
		});
		setLayout(new FormLayout());
		
		createWidgetsComponentsGroup();
		createWidgetsEventsGroup();
		createWidgetsCreateEventGroup();
		createWidgetsInfosGroup();
		createWidgetsButtons();
		
		createWidgetAlignmentsAndSizes();

		setTabList(new Control[]{grpComponents, grpEvents, btnClose});
		
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
		setSize(1009, 527);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
