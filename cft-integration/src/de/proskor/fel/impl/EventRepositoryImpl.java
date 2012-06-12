package de.proskor.fel.impl;

import java.util.ArrayList;

import de.proskor.fel.EventRepository;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.EventType;
import de.proskor.model.Repository;
import de.proskor.ea.model.EARepository;

public class EventRepositoryImpl implements EventRepository {
	private ArrayList<EventTypeContainer> eventTypeContainers;
	private ArrayList<EventType> eventTypes;
	private Repository eaRepository;
	
	@Override
	public ArrayList<EventType> getEventTypes() {
		return eventTypes;
	}
	
	@Override
	public ArrayList<EventTypeContainer> getEventTypeContainers() {
		return eventTypeContainers;
	}
	
	public EventRepositoryImpl() {
		eventTypeContainers = new ArrayList<EventTypeContainer>();
		eventTypes = new ArrayList<EventType>();
		
//		eaRepository = // TODO: Instanz von Repository? Repository.getCurrent() existiert nicht in Klasse, sondern nur in Objekt Repository --> Java Access? 
		
		reloadFromEA();
	}

	public static EventRepository getInstance() {
		//TODO: Complete.
		return null;
	}

	@Override
	public boolean writeEventType(EventType e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean writeEventInstance(EventType e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reloadFromEA() {
		// TODO Auto-generated method stub		
	}
}
