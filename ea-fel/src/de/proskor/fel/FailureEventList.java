package de.proskor.fel;

import de.proskor.fel.impl.EventTypeImpl;
import de.proskor.fel.impl.EventContainerImpl;
import de.proskor.fel.impl.EventInstanceImpl;
import de.proskor.fel.ui.FailureEventListGuiHandler;
import de.proskor.fel.ui.FailureEventListGui.CreationResult;

public class FailureEventList {
	private FailureEventListGuiHandler felGuiHandler;
	
	public FailureEventList(boolean askUserForNameOfNewEvent) {
		boolean readonly = !askUserForNameOfNewEvent;
		felGuiHandler = new FailureEventListGuiHandler(readonly);
	}
	
	public CreationResult showEventList() {
		felGuiHandler.updateGui();
		return felGuiHandler.showEventList();
	}
	
	/**
	 * Create {@link EventContainerImpl EventCFTs} containing the {@link EventInstanceImpl EventInstances}
	 * and add them here. Call {@link #showEventList()} to open the GUI and get the <i>Events</i>
	 * and <i>EventInstances</i> created by the User (if any). 
	 * @param eventCft
	 */
	public void addEventCFT(EventInstanceContainer eventCft) {
		felGuiHandler.addEventCFT(eventCft);
	}
	
	public static void main(String[] args) {
		System.out.println("App started.");
		
		FailureEventListGuiHandler fel = new FailureEventListGuiHandler(false);
		
		// Dummy-Eintr�ge erzeugen:
		EventType eventCommon = new EventTypeImpl("Event " + "common", "Yoda", "The force is strong.",  "{common"+0+"}", 0);
		int id=1;
		EventInstanceContainer[] cft = new EventContainerImpl[4];
		for(int i=0; i<cft.length; i++) {
			cft[i] = new EventContainerImpl("CFT " + i);
			
			for(int k=0; k<2; k++) { 
				EventType event = new EventTypeImpl("Event " + i+"."+k, "Yoda", "-",  "{Event"+id+"}", id);
				EventInstance evInst = new EventInstanceImpl(event, cft[i], "Darth Vader",  "Anything", "{EventInstance"+id+"}", 0);
				System.out.println("- " + evInst);
			}
			
			fel.addEventCFT(cft[i]);
			id++;
		}
		
		for(int i=0; i<cft.length; i++) { 
			EventInstance evInst = new EventInstanceImpl(eventCommon, cft[i], "Darth Vader",  "Anything", "{common"+0+"}", 0);
			System.out.println("- " + evInst);
			id++;
		}

		
		fel.updateGui();
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
