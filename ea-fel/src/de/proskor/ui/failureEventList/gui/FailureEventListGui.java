package de.proskor.ui.failureEventList.gui;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import de.proskor.ui.failureEventList.Event;
import de.proskor.ui.failureEventList.EventInstance;


public class FailureEventListGui extends Shell {
	interface GuiHandler {
		public void onComboBoxSelectionChanged(ModifyEvent e);
		public void onTreeItemSelected(TreeItem item);
		public void onTextNewEventsNameChanged(String name);
		public void onTextFilterEventByNameChanged(String text);
		public Event getEventByName(String name);
		public Event createNewEvent(String newEventsName);
		public boolean eventExists(String newEventsName);
		public EventInstance createNewEventInstance(String eventName);
	}
	
	public static class CreationResult {
		public ArrayList<Event> createdEvents = new ArrayList<Event>();
		public ArrayList<EventInstance> createdEventInstances = new ArrayList<EventInstance>();
	}
	
	public static enum GuiCloseMethod { userOK, userCancel, shellClose };
	
	private GuiCloseMethod guiCloseMethod;
	private final GuiHandler guiHandler;
	private boolean shellIsClosing;
	private CreationResult creationResult;
	private boolean readonly;

	
	private Button btnCancel;
	private Button btnOK;
	private Combo comboEventListView;
	private Tree treeEventList;
	private Button btnExpandAll;
	private Button btnCollapseAll;
	private TreeColumn trclmnEventName;
	private TreeColumn trclmnParentCft;
	private TreeColumn trclmnInstancesCount;
	private TreeColumn trclmnIdGuid;
	private Text textNewEventsName;
	private Text textFilterEventName;
	private Button btnCreateEvent;
	private Button btnCreateEventInstance;


	public GuiHandler getGuiHandler() {
		return guiHandler;
	}

	public boolean isShellIsClosing() {
		return shellIsClosing;
	}

	public Button getBtnCreateEvent() {
		return btnCreateEvent;
	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	public Button getBtnCreateEventInstance() {
		return btnCreateEventInstance;
	}

	public Button getBtnCreateInstance() {
		return btnCreateEventInstance;
	}

	public CreationResult getCreationResult() {
		return creationResult;
	}

	public Text getTextFilterEventName() {
		return textFilterEventName;
	}

	public GuiCloseMethod getGuiCloseMethod() {
		return guiCloseMethod;
	}
	
	public Button getBtnOK() {
		return btnOK;
	}

	public Text getTextNewEventsName() {
		return textNewEventsName;
	}

	public GuiHandler getGuiListener() {
		return guiHandler;
	}

	public Tree getTreeEventList() {
		return treeEventList;
	}

	public TreeColumn getTrclmnIdGuid() {
		return trclmnIdGuid;
	}

	public Button getBtnCancelEventCreation() {
		return btnCancel;
	}

	public Button getBtnUseExistingEvent() {
		return btnOK;
	}

	public Combo getComboEventListView() {
		return comboEventListView;
	}

	public Tree getEventTree() {
		return treeEventList;
	}
	
	public Button getBtnExpandAll() {
		return btnExpandAll;
	}

	public Button getBtnCollapseAll() {
		return btnCollapseAll;
	}

	public TreeColumn getTrclmnEventName() {
		return trclmnEventName;
	}

	public TreeColumn getTrclmnParentCft() {
		return trclmnParentCft;
	}

	public TreeColumn getTrclmnInstancesCount() {
		return trclmnInstancesCount;
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public FailureEventListGui(GuiHandler guiListener, boolean readonly) {
		super(SWT.CLOSE | SWT.MIN | SWT.TITLE);
		this.guiHandler = guiListener;
		this.readonly = readonly;
		
		createComponents();
	}

	/**
	 * Create the shell.
	 * @param display
	 * @wbp.parser.constructor
	 */
	public FailureEventListGui(GuiHandler guiListener, boolean readonly, Display display) {
		super(display, SWT.SHELL_TRIM);
		this.guiHandler = guiListener;
		this.readonly = readonly;
		
		createComponents();
	}
	
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
		setGuiReadonlyMode(readonly);
		
		creationResult = new CreationResult();

		// Wichtige Aktion! Nur so werden die OnChange-Events ausgel�st, welche das GUI anpassen.
		if (!readonly) {
			textNewEventsName.setText("New Event");
			textNewEventsName.selectAll();
		}
	}
	
	private void createComponents() {	
		/* Das GUI kann auf folgende Weisen geschlossen werden:
		 * a) durch einen Button (OK oder Cancel)
		 * b) durch einen Doppel-Klick im Tree
		 * c) durch den Klick auf das "Schlie�en" Symbol des Frames
		 * 
		 * Da man c) nicht explizit feststellen kann, kann man also 
		 * lediglich ein globals "OnClose" Event erstellen und darauf reagieren.
		 * 
		 * Wird das GUI nun geschlossen, OHNE dass a) oder b) (beides ist explizit erfassbar)
		 * die Ursache war, wird angenommen dass das GUI per "ShellClose" (c) geschlossen wurde.
		 */
		guiCloseMethod = GuiCloseMethod.shellClose;
		
		this.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				if (shellIsClosing)
					return;
				else
					shellIsClosing = true;
				
				if (guiCloseMethod.equals(GuiCloseMethod.shellClose))
					finishAndCloseGui(GuiCloseMethod.shellClose);
			}
		});
		
		btnCancel = new Button(this, SWT.CENTER);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				finishAndCloseGui(GuiCloseMethod.userCancel);
			}
		});
		btnCancel.setBounds(629, 357, 75, 25);
		btnCancel.setText("Cancel");
		
		btnOK = new Button(this, SWT.NONE);
		btnOK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				finishAndCloseGui(GuiCloseMethod.userOK);
			}
		});
		btnOK.setBounds(548, 357, 75, 25);
		btnOK.setText("OK");

		comboEventListView = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
		comboEventListView.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				guiHandler.onComboBoxSelectionChanged(e);
			}
		});
		comboEventListView.setItems(new String[] {});
		comboEventListView.setBounds(70, 9, 200, 23);
		
		treeEventList = new Tree(this, SWT.BORDER | SWT.FULL_SELECTION);
		treeEventList.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// RETURN im Tree entspricht dem Klick auf den "OK" Button				
				if (keyEventIsReturn(e))
					finishAndCloseGui(GuiCloseMethod.userOK);
			}
		});
		treeEventList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				finishAndCloseGui(GuiCloseMethod.userOK);
			}
		});
		treeEventList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				guiHandler.onTreeItemSelected((TreeItem)e.item);
			}
		});
		treeEventList.setLinesVisible(true);
		treeEventList.setBounds(10, 39, 694, 312);
		treeEventList.setHeaderVisible(true);
		
		trclmnEventName = new TreeColumn(treeEventList, SWT.LEFT);
		trclmnEventName.setText("Event Name");
		trclmnEventName.setWidth(200);
		
		trclmnParentCft = new TreeColumn(treeEventList, SWT.CENTER);
		trclmnParentCft.setText("Parent CFT(s)");
		trclmnParentCft.setWidth(200);
		
		trclmnInstancesCount = new TreeColumn(treeEventList, SWT.LEFT);
		trclmnInstancesCount.setText("# Instances");
		trclmnInstancesCount.setWidth(50);
		
		trclmnIdGuid = new TreeColumn(treeEventList, SWT.LEFT);
		trclmnIdGuid.setWidth(240);
		trclmnIdGuid.setText("ID / GUID");
		
		Menu menuTreeEventList = new Menu(treeEventList);
		treeEventList.setMenu(menuTreeEventList);
		
		btnExpandAll = new Button(this, SWT.NONE);
		btnExpandAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				opExpandTreeEntries(true);
			}
		});
		btnExpandAll.setBounds(548, 8, 75, 25);
		btnExpandAll.setText("Expand All");
		
		btnCollapseAll = new Button(this, SWT.NONE);
		btnCollapseAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				opExpandTreeEntries(false);
			}
		});
		btnCollapseAll.setBounds(629, 8, 75, 25);
		btnCollapseAll.setText("Collapse All");
		
		Label lblFilterByCft = new Label(this, SWT.NONE);
		lblFilterByCft.setBounds(10, 13, 75, 15);
		lblFilterByCft.setText("Select CFT:");
		
		Label lblNewEventsName = new Label(this, SWT.NONE);
		lblNewEventsName.setBounds(10, 362, 104, 15);
		lblNewEventsName.setText("New Events Name:");
		
		textNewEventsName = new Text(this, SWT.BORDER);
		textNewEventsName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// RETURN im Text-Feld entspricht dem Klick auf den "OK" Button				
				if (keyEventIsReturn(e))
					finishAndCloseGui(GuiCloseMethod.userOK);
			}
		});
		textNewEventsName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				guiHandler.onTextNewEventsNameChanged(textNewEventsName.getText());
			}
		});
		textNewEventsName.setBounds(114, 359, 165, 21);
		
		Label lblFilterByEvent = new Label(this, SWT.NONE);
		lblFilterByEvent.setBounds(284, 12, 67, 15);
		lblFilterByEvent.setText("Filter Events:");
		
		textFilterEventName = new Text(this, SWT.BORDER);
		textFilterEventName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				guiHandler.onTextFilterEventByNameChanged(textFilterEventName.getText());
			}
		});
		textFilterEventName.setBounds(352, 9, 180, 21);
		
		btnCreateEvent = new Button(this, SWT.NONE);
		btnCreateEvent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				actionCreateEvent();
			}
		});
		btnCreateEvent.setBounds(285, 357, 90, 25);
		btnCreateEvent.setText("Create Event");
		
		btnCreateEventInstance = new Button(this, SWT.NONE);
		btnCreateEventInstance.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				actionCreateEventInstance();
			}
		});
		btnCreateEventInstance.setBounds(381, 357, 90, 25);
		btnCreateEventInstance.setText("Create Instance");
		setTabList(new Control[]{textNewEventsName, treeEventList, comboEventListView, btnOK, btnCancel, btnExpandAll, btnCollapseAll});
		createContents();
	}
	
	protected void actionCreateEvent() {
		String newEventsName = textNewEventsName.getText();

		Event newEvent;
		if (!guiHandler.eventExists(newEventsName)) 
			newEvent = guiHandler.createNewEvent(newEventsName); // kann ebenfalls NULL liefern.
		else
			newEvent = null;

		if (newEvent != null)
			creationResult.createdEvents.add(newEvent);
		}	
	
	protected void actionCreateEventInstance() {
		String eventName = textNewEventsName.getText();
		
		EventInstance eventInstance = guiHandler.createNewEventInstance(eventName);
		
		if (eventInstance != null) 
			creationResult.createdEventInstances.add(eventInstance);
		}

	private boolean keyEventIsReturn(KeyEvent e) {
		// 13 == RETURN; 16777296 == NUM_RETURN
		return e.keyCode == 13 || e.keyCode == 16777296;
	}

	protected void finishAndCloseGui(GuiCloseMethod closeMethod) {
		guiCloseMethod = closeMethod;
		
		if (closeMethod != GuiCloseMethod.userOK)
			creationResult = new CreationResult(); // Es wird ein 'leeres' Ergebnis geliefert. Jedoch NICHT NULL!
			
		close();
	}
	
	public void opExpandTreeEntries(boolean expanded) {
		for(TreeItem item : treeEventList.getItems())
			item.setExpanded(expanded);
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Failure Event List");
		setSize(720, 419);
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	private void setGuiReadonlyMode(boolean readonly) {
		boolean enabled = !readonly;
		
		btnCancel.setEnabled(enabled);
		btnOK.setEnabled(enabled);
		btnCreateEvent.setEnabled(enabled);
		btnCreateEventInstance.setEnabled(enabled);
		textNewEventsName.setEnabled(enabled);
	}
}
