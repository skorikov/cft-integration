package de.proskor.fel.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.proskor.fel.container.EventInstanceContainer;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.EventInstance;
import de.proskor.fel.event.EventType;
import de.proskor.fel.ui.FailureEventListGui.GuiHandler;

public class FailureEventListGuiHandler implements GuiHandler {
	private static class EventFilter {
		private String eventNameFilter = "";
		
		public void setEventNameFilter(String eventNameFilter) {
			this.eventNameFilter = eventNameFilter.toLowerCase();
		}
		
		public EventFilter() {
		}
		
		public boolean eventConformsToFilter(EventType event) {
			String eventNameLower = event.getName().toLowerCase();
			boolean nameOk = eventNameLower.contains(eventNameFilter);
			
			return nameOk;
		}
	}
	
	
	private final int comboListViewIndex_allCFTs = 0;
	private final int comboListViewIndex_firstCFTOffset = 1;
	
	private FailureEventListGui gui;
	private boolean uptodate = false; // genau dann TRUE, wenn mind. einmal geupdated und nicht gerade dabei.
//	private boolean onlyShowEvents;
	private boolean autoJumpToEventBySpecifiedName;
	private EventFilter eventFilter;
	
	private ArrayList<EventInstanceContainer> cfts;
	private HashMap<String, TreeItem> currentEventsInGui;
	private HashMap<String, EventType> eventsNameMap;
	
	public FailureEventListGuiHandler(boolean onlyShowEvents) {
	//	this.onlyShowEvents = onlyShowEvents;
		
		eventFilter = new EventFilter();
		currentEventsInGui = new HashMap<String, TreeItem>();
		cfts = new ArrayList<EventInstanceContainer>();
		gui = new FailureEventListGui(this, onlyShowEvents);

		autoJumpToEventBySpecifiedName = true;
	}
	
	public void showEventList() {
		eventsNameMap = updateEventMap(new HashMap<String, EventType>());
		gui.show();
	}
	
	/**
	 * Performs {@link #addEventInstanceContainer(EventInstanceContainer)} on all {@link EventInstanceContainer}
	 * objects contained in the {@link EventTypeContainer}.   
	 */
	public void addEventTypeContainer(EventTypeContainer container) {
		for(EventInstanceContainer instContainer : container.getInstances())
			addEventInstanceContainer(instContainer);
	}
	
	public void addEventInstanceContainer(EventInstanceContainer instanceContainer) {
		cfts.add(instanceContainer);
	}
	
	private void updateGuiTree() {
		Tree tree = gui.getEventTree();
		tree.removeAll();
		
		EventInstanceContainer selectedCFT = getSelectedCftFromComboEventList();
		if (selectedCFT == null) {
			for(EventInstanceContainer cft : cfts)
				updateGuiTree_addCft(cft);
		} else {
			updateGuiTree_addCft(selectedCFT);
		}
	}
	
	private String[] getTreeItemEventName(EventType event) {
//		String namesOfCftsContainingInstances = "";
		
		return new String[] {
				event.getName(), 
				event.getContainer().getName(), 
				event.getInstances().size()+"", 
				event.getId() +" / "+ event.getGuid()
		};
	}
	
	private String[] getTreeItemEventInstanceName(EventInstance eventInstance) {
		return new String[] {
				"<Instance>", 
				eventInstance.getContainer().getName(), 
				"", 
				eventInstance.getId() +" / "+ eventInstance.getGuid()
		};
	}
	
	private void updateGuiTree_addCft(EventInstanceContainer cft) {
		Tree tree = gui.getEventTree();

		for(EventInstance eventInstance: cft.getEvents()) {
			if (eventFilter.eventConformsToFilter(eventInstance.getType())) {
				/* Wenn alle Events aller CFTs eingetagen werden, w�rden 'Common Events' mehrfach eingetragen werden.
				 */

				TreeItem item = currentEventsInGui.get(eventInstance.getType().getName());

				if (item == null) { 
					item = new TreeItem(tree, SWT.NONE);
					item.setText(getTreeItemEventName(eventInstance.getType()));

					currentEventsInGui.put(eventInstance.getType().getName(), item);
				}

				TreeItem subItem = new TreeItem(item, SWT.NONE);
				subItem.setText(getTreeItemEventInstanceName(eventInstance));
			}
		}
	}
	
	public void updateGui() {
		uptodate = false;
		
		currentEventsInGui.clear();
		updateGuiComboEventList();
		updateGuiTree();
		
		uptodate = true;
	}
	
	private void updateGuiComboEventList() {
		int origItemsCount = gui.getComboEventListView().getItemCount();
		int origIndex = -1;
		
		if (origItemsCount != 0)
			origIndex = gui.getComboEventListView().getSelectionIndex();
		
		String[] cftNames = new String[cfts.size()+1];
		cftNames[comboListViewIndex_allCFTs] = "== All parent and child CFTs ==";
		for(int i=comboListViewIndex_firstCFTOffset; i<cfts.size() + comboListViewIndex_firstCFTOffset; i++)
			cftNames[i] = cfts.get(i-comboListViewIndex_firstCFTOffset).getName();
		
		System.out.println("cftNames: ");
		for(String name : cftNames)
			System.out.println(name);
		
		int newItemsCount = gui.getComboEventListView().getItemCount();
		gui.getComboEventListView().setItems(cftNames);
		
		// Falls sich Anzahl an CFTs ge�ndert hat, wird "ALL CFTs" gew�hlt. Urspr�ngliche Auswahl sonst. 
		if ((origItemsCount == newItemsCount) && (origIndex != -1))
			gui.getComboEventListView().select(origIndex);
		else
			gui.getComboEventListView().select(0);
	}
	
	private void setSelectedCftFromComboEventList(EventInstanceContainer cft) {
		if (cft == null)
			gui.getComboEventListView().select(comboListViewIndex_allCFTs);
		else
			gui.getComboEventListView().select(cfts.indexOf(cft) + comboListViewIndex_firstCFTOffset);
	}
	
	private EventInstanceContainer getSelectedCftFromComboEventList() {
		int selectedCftIndex = gui.getComboEventListView().getSelectionIndex();
		
		if (selectedCftIndex == comboListViewIndex_allCFTs)
			return null;
		else
			return cfts.get(selectedCftIndex-comboListViewIndex_firstCFTOffset);
	}
	
	private void guiSelectEvent(EventType event) {
		// Auswahl wird �ber Text durchgef�hrt, da die Listen-Indizes durch die Sortierung nicht stimmen m�ssen.
		for(TreeItem eventItem : gui.getTreeEventList().getItems()) {
			if (eventItem.getText().equals(event.getName()))
				gui.getTreeEventList().select(eventItem);
		}
	}

	@Override
	public void onTreeItemSelected(TreeItem item) {
		 TreeItem eventTopItem = item;

		// Falls Item ein Parent hat, ist eine Instanz ausgew�hlt. ==> Parent ist "echtes" Event mit "richtigem" Namen. (Instancen hei�en nur "<instance>")
		if (item.getParentItem() != null)
			eventTopItem = item.getParentItem();
		
		autoJumpToEventBySpecifiedName = false;
		gui.getTextNewEventsName().setText(eventTopItem.getText());
		autoJumpToEventBySpecifiedName = true;
	}

	@Override
	public void onComboBoxSelectionChanged(ModifyEvent e) {
		if (uptodate)
			updateGui();
	}
	
	private void guiJumpToEventBySpecifiedName(String name) {
		EventType event = getEventFromGuiByName(name); // Momentan sichtbare durchsuchen
		if (event != null) {
			guiSelectEvent(event);
		}  else {
			event = getEventByName(name); // Alle durchsuchen
			
			if (event != null) {
				setSelectedCftFromComboEventList(null); // Alle Events aller CFT listen.

				// Jetzt wurde das GUI durch as On-Change-Event der ComboBox geupdated.
			} else {
				return;
			}
		}
		
		guiSelectEvent(event);
	}
	
	private EventType getEventFromGuiByName(String name) {
		if (currentEventsInGui.get(name) != null) // Falls das Event im GUI eingetragen ist (Eintrag ist vom Typ TreeItem)...
			return eventsNameMap.get(name); // Wird das Event-Objekt aus der vollst�ndigen Liste geholt.
		else
			return null;
	}

	@Override
	public void onTextNewEventsNameChanged(String name) {
		if (name == "")
			return;
		
		boolean eventExists = eventExists(name);
		gui.getBtnCreateEvent().setEnabled(!eventExists);
		gui.getBtnCreateEventInstance().setEnabled(eventExists);			
		
		if (autoJumpToEventBySpecifiedName)
			guiJumpToEventBySpecifiedName(name);
	}

	@Override
	public void onTextFilterEventByNameChanged(String text) {
		eventFilter.setEventNameFilter(text);
		updateGui();
	}
	
	private HashMap<String, EventType> updateEventMap(HashMap<String, EventType> map) {
		for(EventInstanceContainer cft : cfts)
			for (EventInstance instance : cft.getEvents())
				if (!map.containsKey(instance.getType().getName()))
					map.put(instance.getType().getName(), instance.getType());
		
		return map;
	}

	@Override
	public EventType getEventByName(String name) {
		return eventsNameMap.get(name);
	}

	@Override
	public EventType createNewEvent(String newEventsName) {
//		FailureEventListCreateEventGui creator = new FailureEventListCreateEventGui();
		
//		return creator.createEvent(newEventsName); //TODO: Complete
		return null;
	}	
	
	@Override
	public EventInstance createNewEventInstance(String eventName) {
		EventType event = eventsNameMap.get(eventName);
		if (event == null)
			return null;
		
		FailureEventListCreateEventGui creator = new FailureEventListCreateEventGui();
		return creator.createEventInstance(event, cfts, getSelectedCftFromComboEventList());
	}

	@Override
	public boolean eventExists(String newEventsName) {
		return getEventByName(newEventsName) != null;
	}
}
