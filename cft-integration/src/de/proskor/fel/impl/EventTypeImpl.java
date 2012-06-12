package de.proskor.fel.impl;

import java.util.ArrayList;
import java.util.List;

import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.EventInstance;
import de.proskor.fel.event.EventType;


public class EventTypeImpl implements EventType {
	private String name;
	private String guid;
	private int id;
	private String author;
	private String description;

	private List<EventInstance> instances;
	private EventTypeContainer container;

	@Override
	public String getName() {
		return name;
	}

	
	@Override
	public String getGuid() {
		return guid;
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getAuthor() {
		return author;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public List<EventInstance> getInstances() {
		return instances;
	}

	@Override
	public void addInstance(EventInstance eventInstance) {
		if (!instances.contains(eventInstance))
			instances.add(eventInstance);
	}

	@Override
	public EventTypeContainer getContainer() {
		return container;
	}

	@Override
	public String toString() {
		return "Event: "+name+"; Container: "+container+";  ID: "+id+";  guid: "+guid + "; author: " + author + "; description: " + description;
	}
	
	public EventTypeImpl(String name, EventTypeContainer container, String author, String description, String guid, int id) {
		this.name = name;
		this.container = container;		
		this.author = author;
		this.description = description;
		this.guid = guid;
		this.id = id;
		instances = new ArrayList<EventInstance>();
		
		container.addEvent(this);
	}
}