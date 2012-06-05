package de.proskor.ui.failureEventList.gui;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.proskor.ui.failureEventList.Event;
import de.proskor.ui.failureEventList.EventCFT;
import de.proskor.ui.failureEventList.EventInstance;
import de.proskor.ui.failureEventList.gui.FailureEventListGui.CreationResult;
import de.proskor.ui.failureEventList.gui.FailureEventListGui.GuiHandler;

public class FailureEventListGuiHandler implements GuiHandler {
	private static class EventFilter {
		private String eventNameFilter = "";
		
		public void setEventNameFilter(String eventNameFilter) {
			this.eventNameFilter = eventNameFilter;
		}
		
		public EventFilter() {
		}
		
		public boolean eventConformsToFilter(Event event) {
			boolean nameOk = event.getName().contains(eventNameFilter);
			
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
	
	private ArrayList<EventCFT> cfts;
	private HashMap<String, TreeItem> currentEventsInGui;
	private HashMap<String, Event> eventsNameMap;
	
	public FailureEventListGuiHandler(boolean onlyShowEvents) {
	//	this.onlyShowEvents = onlyShowEvents;
		
		eventFilter = new EventFilter();
		currentEventsInGui = new HashMap<String, TreeItem>();
		cfts = new ArrayList<EventCFT>();
		gui = new FailureEventListGui(this, onlyShowEvents);

		autoJumpToEventBySpecifiedName = true;
	}
	
	public CreationResult showEventList() {
		eventsNameMap = updateEventMap(new HashMap<String, Event>());
		gui.show();
		
		return gui.getCreationResult();
	}
	
	public void addEventCFT(EventCFT cft) {
		cfts.add(cft);
	}
	
	private void updateGuiTree() {
		Tree tree = gui.getEventTree();
		tree.removeAll();
		
		EventCFT selectedCFT = getSelectedCftFromComboEventList();
		if (selectedCFT == null) {
			for(EventCFT cft : cfts)
				updateGuiTree_addCft(cft);
		} else {
			updateGuiTree_addCft(selectedCFT);
		}
	}
	
	private String[] getTreeItemEventName(Event event) {
		String namesOfCftsContainingInstances = "";
		
		for (EventInstance inst : event.getInstances()) {
			if (!namesOfCftsContainingInstances.equals(""))
				namesOfCftsContainingInstances = namesOfCftsContainingInstances + ", " + inst.getCft().getName(); 
			else
				namesOfCftsContainingInstances = inst.getCft().getName();
		}
		
		return new String[] {
				event.getName(), 
				namesOfCftsContainingInstances, 
				event.getInstances().size()+"", 
				event.getId() +" / "+ event.getGuid()
		};
	}
	
	private String[] getTreeItemEventInstanceName(EventInstance eventInstance) {
		return new String[] {
				"<Instance>", 
				eventInstance.getCft().getName(), 
				"", 
				eventInstance.getId() +" / "+ eventInstance.getGuid()
		};
	}
	
	private void updateGuiTree_addCft(EventCFT cft) {
		Tree tree = gui.getEventTree();

		for(EventInstance eventInstance: cft.getEventInstances()) {
			if (eventFilter.eventConformsToFilter(eventInstance.getEvent())) {
				/* Wenn alle Events aller CFTs eingetagen werden, w�rden 'Common Events' mehrfach eingetragen werden.
				 */

				TreeItem item = currentEventsInGui.get(eventInstance.getEvent().getName());

				if (item == null) { 
					item = new TreeItem(tree, SWT.NONE);
					item.setText(getTreeItemEventName(eventInstance.getEvent()));

					currentEventsInGui.put(eventInstance.getEvent().getName(), item);
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
		
		int newItemsCount = gui.getComboEventListView().getItemCount();
		gui.getComboEventListView().setItems(cftNames);
		
		// Falls sich Anzahl an CFTs ge�ndert hat, wird "ALL CFTs" gew�hlt. Urspr�ngliche Auswahl sonst. 
		if ((origItemsCount == newItemsCount) && (origIndex != -1))
			gui.getComboEventListView().select(origIndex);
		else
			gui.getComboEventListView().select(0);
	}
	
	private void setSelectedCftFromComboEventList(EventCFT cft) {
		if (cft == null)
			gui.getComboEventListView().select(comboListViewIndex_allCFTs);
		else
			gui.getComboEventListView().select(cfts.indexOf(cft) + comboListViewIndex_firstCFTOffset);
	}
	
	private EventCFT getSelectedCftFromComboEventList() {
		int selectedCftIndex = gui.getComboEventListView().getSelectionIndex();
		
		if (selectedCftIndex == comboListViewIndex_allCFTs)
			return null;
		else
			return cfts.get(selectedCftIndex-comboListViewIndex_firstCFTOffset);
	}
	
	private void guiSelectEvent(Event event) {
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
		Event event = getEventFromGuiByName(name); // Momentan sichtbare durchsuchen
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
	
	private Event getEventFromGuiByName(String name) {
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
	
	private HashMap<String, Event> updateEventMap(HashMap<String, Event> map) {
		for(EventCFT cft : cfts)
			for (EventInstance instance : cft.getEventInstances())
				if (!map.containsKey(instance.getEvent().getName()))
					map.put(instance.getEvent().getName(), instance.getEvent());
		
		return map;
	}

	@Override
	public Event getEventByName(String name) {
		return eventsNameMap.get(name);
	}

	@Override
	public Event createNewEvent(String newEventsName) {
		FailureEventListCreateEventGui creator = new FailureEventListCreateEventGui();
		return creator.createEvent(newEventsName);
	}	
	
	@Override
	public EventInstance createNewEventInstance(String eventName) {
		Event event = eventsNameMap.get(eventName);
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
