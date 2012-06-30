package de.proskor.fel.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import de.proskor.fel.EventRepository;
import de.proskor.fel.Type;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.ui.Fitlers.EventTypeContainerFilter;
import de.proskor.fel.ui.Fitlers.EventTypeFilter;
import de.proskor.fel.ui.GuiRepository.EventTypeContainerHandler;
import de.proskor.fel.ui.GuiRepository.EventTypesHandler;

public class FailureEventListGui extends Shell {
	private final FailureEventList failureEventList;
	private final EventRepository eventRepository;

	private final GuiHandler guiHandler;
	
	public Button btnClose;
	public Text textNewEventsName;
	public Text textEventFilterByFieldsMatch;
	public Button btnCreateEvent;
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
	private Button btnComponentsSelectByFieldsMatch;


	private Tree treeEvents;
	private Combo comboEventFilterMode;
	private Combo comboComponentsSelectByFieldsMatch;
	private Button btnComponentsSelectSub;
	private Button btnComponentsSelectSuper;

	/**
	 * @wbp.parser.constructor
	 */
	public FailureEventListGui(FailureEventList failureEventList) {
		super(SWT.CLOSE | SWT.MIN | SWT.TITLE);
		
		createComponents();
		
		this.failureEventList = failureEventList;
		this.eventRepository = failureEventList.getRepository();
		
		guiHandler = new GuiHandler();
		guiHandler.updateGui();
	}
	
	private class GuiHandler {
		private final EventTypesHandler eventsHandler;
		private final EventTypeContainerHandler containerHandler;
		private final FailureEventListGui gui = FailureEventListGui.this;
		private final EventTypeFilter eventTypeFilter;
		private final EventTypeContainerFilter containerFilter;
		
		public GuiHandler() {
			eventsHandler = new EventTypesHandler(treeEvents);
			containerHandler = new EventTypeContainerHandler(treeComponents);
			eventTypeFilter = new EventTypeFilter("", comboEventFilterMode, textEventFilterByFieldsMatch);
			containerFilter = new EventTypeContainerFilter("", comboComponentsSelectByFieldsMatch, textComponentsSelectByFieldsMatch);
			
			configGuiForFirstUse();
		}
		
		public void configGuiForFirstUse() {
			comboEventFilterMode.setItems(eventTypeFilter.getFilterModeNames());
			comboEventFilterMode.select(0);
			
			comboComponentsSelectByFieldsMatch.setItems(containerFilter.getFilterModeNames());
			comboComponentsSelectByFieldsMatch.select(0);
		}
		
		private void updateGui() {
			updateContainerList();
		}	
		
		private void updateContainerList() {
			containerHandler.clear();

			for (EventTypeContainer c : eventRepository.getEventTypeContainers()) {
				containerHandler.addContainerAndSubContainers(c);
			}
			
			if (treeComponents.getItemCount() > 0)
				treeComponents.select(treeComponents.getItem(0));
		}
		
		public void selectContainerByFieldMatch() {
			containerFilter.applyGuiFilterConfig();
			
			ArrayList<TreeItem> selection = new ArrayList<TreeItem>();
			for (EventTypeContainer c : containerHandler.getContainers()) {
				if (containerFilter.typeConformsToFilter(c)) {
					TreeItem cItem = containerHandler.getTreeItemByType(c);
					selection.add(cItem);
				}
			}
			
			TreeItem[] selectionItems = DataUtils.treeItemListToArray(selection);				
			treeComponents.setSelection(selectionItems);
		}

		public void selectSubComponents() {
			selectSubComponentsEx();
			expandSelectedTreeItems(treeComponents, true);
		}
			
		private void selectSubComponentsEx() {
			ArrayList<TreeItem> selection = new ArrayList<TreeItem>();
			ArrayList<TreeItem> currentSelection = DataUtils.treeItemArrayToList(treeComponents.getSelection()); 
			selection.addAll(currentSelection);
			
			for(TreeItem selectedContainerItem : currentSelection) {
				EventTypeContainer container = (EventTypeContainer)containerHandler.getTypeByTreeItem(selectedContainerItem);
				List<EventTypeContainer> subContainers = DataUtils.getAllSubContainer(container);
				
				for(EventTypeContainer sub : subContainers)
					if (!selection.contains(sub))
						selection.add(containerHandler.getTreeItemByType(sub));
			}
			
			TreeItem[] selectionArray = DataUtils.treeItemListToArray(selection);
			treeComponents.setSelection(selectionArray);
		}

		public void selectSuperComponents() {
			ArrayList<TreeItem> selection = new ArrayList<TreeItem>();
			ArrayList<TreeItem> currentSelection = DataUtils.treeItemArrayToList(treeComponents.getSelection()); 
			selection.addAll(currentSelection);
			
			for(TreeItem selectedContainerItem : currentSelection) {
				EventTypeContainer container = (EventTypeContainer)containerHandler.getTypeByTreeItem(selectedContainerItem);
				List<EventTypeContainer> superContainers = DataUtils.getAllSuperContainer(container);
				
				for(EventTypeContainer parent : superContainers)
					if (!selection.contains(parent))
						selection.add(containerHandler.getTreeItemByType(parent));
			}
			
			TreeItem[] selectionArray = DataUtils.treeItemListToArray(selection);
			treeComponents.setSelection(selectionArray);
		}
		
		
		public void expandTreeItems(Tree tree, boolean expanded) {
			for(TreeItem item : tree.getItems())
				expandTreeItem(item, expanded);
		}
		
		public void expandTreeItem(TreeItem item, boolean expanded) {
			item.setExpanded(expanded);
			
			for(TreeItem subItem : item.getItems()) 
				expandTreeItem(subItem, expanded);
		}
		
		public void expandSelectedTreeItems(Tree tree, boolean expanded) {
			TreeItem[] items = tree.getSelection();
			
			for(TreeItem item : items)
				item.setExpanded(expanded);
		}
	}
	
	private static class DataUtils {
		public static List<EventTypeContainer> getAllSubContainer(EventTypeContainer container) {
			ArrayList<EventTypeContainer> subContainerList = new ArrayList<EventTypeContainer>();
			
			for(EventTypeContainer sub : container.getChildren()) {
				subContainerList.add(sub);
				
				List<EventTypeContainer> subsSubContainerList = getAllSubContainer(sub);
				subContainerList.addAll(subsSubContainerList);
			}
			
			return subContainerList;
		}
		
		public static List<EventTypeContainer> getAllSuperContainer(EventTypeContainer container) {
			ArrayList<EventTypeContainer> superContainerList = new ArrayList<EventTypeContainer>();
			
			EventTypeContainer parent = container.getParent();
			while(parent != null) {
				superContainerList.add(parent);
				parent = parent.getParent();
			}
			
			return superContainerList;
		}

		public static ArrayList<TreeItem> treeItemArrayToList(TreeItem[] arr) {
			ArrayList<TreeItem> list = new ArrayList<TreeItem>();
			
			for(TreeItem item : arr)
				list.add(item);
			
			return list;
		}
		
		public static TreeItem[] treeItemListToArray(List<TreeItem> list) {
			TreeItem[] arr = new TreeItem[list.size()];
			
			int i=0;
			for(TreeItem item : list) {
				arr[i] = item;
				i++;
			}
			
			return arr;
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
		btnClose.setBounds(886, 357, 98, 25);
		btnClose.setText("Close");

		Label lblNewEventsName = new Label(this, SWT.NONE);
		lblNewEventsName.setBounds(10, 362, 72, 15);
		lblNewEventsName.setText("Create Event:");

		textNewEventsName = new Text(this, SWT.BORDER);
		textNewEventsName.setToolTipText("Name of the event to you wish to create.");
		//	textNewEventsName.addKeyListener(new KeyAdapter() {
		//		@Override
		//		public void keyPressed(KeyEvent e) {
		//			// RETURN im Text-Feld 				
		////			if (keyEventIsReturn(e))
		//		}
		//	});
		textNewEventsName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
//				guiHandler.onTextNewEventsNameChanged(textNewEventsName.getText());
			}
		});
		textNewEventsName.setBounds(84, 359, 313, 21);

		btnCreateEvent = new Button(this, SWT.NONE);
		btnCreateEvent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

			}
		});
		btnCreateEvent.setBounds(403, 357, 90, 25);
		btnCreateEvent.setText("Create Event");

		Group grpEvents = new Group(this, SWT.NONE);
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

		Group grpComponents = new Group(this, SWT.NONE);
		grpComponents.setText(" Components ");
		grpComponents.setBounds(10, 10, 483, 341);

		Button btnComponentsTreeCollapseAll = new Button(grpComponents, SWT.NONE);
		btnComponentsTreeCollapseAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandler.expandTreeItems(treeComponents, false);
			}
		});
		btnComponentsTreeCollapseAll.setText("Collapse All");
		btnComponentsTreeCollapseAll.setBounds(126, 306, 110, 25);

		Button btnComponentsTreeExpandAll = new Button(grpComponents, SWT.NONE);
		btnComponentsTreeExpandAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandler.expandTreeItems(treeComponents, true);
			}
		});
		btnComponentsTreeExpandAll.setText("Expand All");
		btnComponentsTreeExpandAll.setBounds(10, 306, 110, 25);

		treeComponents = new Tree(grpComponents, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
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
					guiHandler.selectContainerByFieldMatch(); // Führt Button-OnClick aus. Kann man bei SWT nicht direkt über Button.onClick() machen.  -.- 
			}
		});
		textComponentsSelectByFieldsMatch.setToolTipText("Selects all components which match the specified name.");
		textComponentsSelectByFieldsMatch.setBounds(40, 18, 221, 21);

		Label lblFind = new Label(grpComponents, SWT.NONE);
		lblFind.setBounds(10, 21, 55, 15);
		lblFind.setText("Find:");
		
		btnComponentsSelectByFieldsMatch = new Button(grpComponents, SWT.NONE);
		btnComponentsSelectByFieldsMatch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandler.selectContainerByFieldMatch();
			}
		});
		btnComponentsSelectByFieldsMatch.setBounds(267, 16, 90, 25);
		btnComponentsSelectByFieldsMatch.setText("Select");
		
		comboComponentsSelectByFieldsMatch = new Combo(grpComponents, SWT.READ_ONLY);
		comboComponentsSelectByFieldsMatch.setItems(new String[] {"[[ Any Field ]]", "Name", "Author", "Description", "ID", "GUID"});
		comboComponentsSelectByFieldsMatch.setBounds(363, 17, 110, 23);
		comboComponentsSelectByFieldsMatch.select(0);
		
		btnComponentsSelectSub = new Button(grpComponents, SWT.NONE);
		btnComponentsSelectSub.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandler.selectSubComponents();
			}
		});
		btnComponentsSelectSub.setToolTipText("Will select all SubComponents of the currently selected component(s).");
		btnComponentsSelectSub.setText("SubComponents");
		btnComponentsSelectSub.setBounds(363, 306, 110, 25);
		
		btnComponentsSelectSuper = new Button(grpComponents, SWT.NONE);
		btnComponentsSelectSuper.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandler.selectSuperComponents();
			}
		});
		btnComponentsSelectSuper.setToolTipText("Will select all SubComponents of the currently selected component(s).");
		btnComponentsSelectSuper.setText("SuperComponents");
		btnComponentsSelectSuper.setBounds(247, 306, 110, 25);
		grpComponents.setTabList(new Control[]{textComponentsSelectByFieldsMatch, treeComponents, btnComponentsTreeExpandAll, btnComponentsTreeCollapseAll});
		setTabList(new Control[]{grpComponents, grpEvents, textNewEventsName, btnCreateEvent, btnClose});
		textEventFilterByFieldsMatch.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
//				guiHandler.onTextFilterEventChanged(textFilterEventName.getText());
			}
		});
		
		
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
		setSize(1000, 420);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
