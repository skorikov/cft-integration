package de.proskor.fel;

import java.util.List;

import de.proskor.fel.container.EventInstanceContainer;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.EventInstance;
import de.proskor.fel.event.EventType;
import de.proskor.fel.impl.EventRepositoryImpl;
import de.proskor.fel.impl.EventTypeContainerImpl;
import de.proskor.fel.impl.EventTypeImpl;
import de.proskor.fel.impl.EventInstanceContainerImpl;
import de.proskor.fel.impl.EventInstanceImpl;
import de.proskor.fel.ui.FailureEventListGuiHandler;
import de.proskor.fel.ui.FailureEventListGui.CreationResult;

public class FailureEventList {
	private FailureEventListGuiHandler felGuiHandler;
	private final EventRepository eventRepository;
	
	public FailureEventList() {
		this.eventRepository = EventRepositoryImpl.getInstance();
		
		reloadRepository();
	}
	
	public void reloadRepository() {
		eventRepository.reloadFromEA();
		felGuiHandler = new FailureEventListGuiHandler(false);
		
		for(EventTypeContainer container : eventRepository.getEventTypeContainers()) 
			felGuiHandler.addEventTypeContainer(container);
	}
	
	public EventTypeContainer getEventTypeContainerByGuid(String guid) {
		for(EventTypeContainer container : eventRepository.getEventTypeContainers()) 
			if (container.getGuid().equals(guid))
				return container;
		
		return null;
	}	
	public EventTypeContainer getEventTypeContainerById(int id) {
		for(EventTypeContainer container : eventRepository.getEventTypeContainers()) 
			if (container.getId() == id)
				return container;
		
		return null;
	}
	
	public CreationResult showEventList() {
		felGuiHandler.updateGui();
		return felGuiHandler.showEventList();
	}
	
	public void createEventInstance() {
		showEventList();
	}
	
	public void createEventType() {
		showEventList();
	}
	
//	/**
//	 * Create {@link EventInstanceContainerImpl EventCFTs} containing the {@link EventInstanceImpl EventInstances}
//	 * and add them here. Call {@link #showEventList()} to open the GUI and get the <i>Events</i>
//	 * and <i>EventInstances</i> created by the User (if any). 
//	 * @param eventCft
//	 */
//	public void addEventCFT(EventInstanceContainer eventCft) {
//		felGuiHandler.addEventCFT(eventCft);
//	}
	
	public static void main(String[] args) {
		System.out.println("App started.");
		
		EventRepository rep = new EventRepositoryImpl();
		EventTypeContainer component = new EventTypeContainerImpl("component1", "{component1}", -1);
		rep.getEventTypeContainers().add(component);
		
		
		EventType eventCommon = new EventTypeImpl("Event " + "common", component, "Yoda", "The force is strong.",  "{common"+0+"}", 0);
		int id=1;
		EventInstanceContainer[] cft = new EventInstanceContainerImpl[4];
		for(int i=0; i<cft.length; i++) {
			cft[i] = new EventInstanceContainerImpl("CFT " + i, "{guid-"+"CFT " + i+"}", -1, component);
			
			for(int k=0; k<2; k++) { 
				EventType event = new EventTypeImpl("Event " + i+"."+k, component, "Yoda", "-",  "{Event"+id+"}", id);
				EventInstance evInst = new EventInstanceImpl(event, cft[i], "Darth Vader",  "Anything", "{EventInstance"+id+"}", 0);
				System.out.println("- " + evInst);
			}
			
			component.addInstance(cft[i]);
			id++;
		}
		
		for(int i=0; i<cft.length; i++) { 
			EventInstance evInst = new EventInstanceImpl(eventCommon, cft[i], "Darth Vader",  "Anything", "{common"+0+"}", 0);
			System.out.println("- " + evInst);
			id++;
		}

		
		FailureEventList fel = new FailureEventList();
		CreationResult result = fel.showEventList();
		
		System.out.println();
		System.out.println("New Events: ");
		for(EventType event : result.createdEvents)
			System.out.println("- " + event);
		
		System.out.println("New Instances: ");
		for(EventInstance instance : result.createdEventInstances)
			System.out.println("- " + instance);
		
		System.out.println("FailureEventList created.");
	}
}
