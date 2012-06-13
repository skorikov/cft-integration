package de.proskor.fel.impl;

import de.proskor.fel.EventType;
import de.proskor.fel.EventInstanceContainer;
import de.proskor.fel.EventInstance;


public class EventInstanceImpl implements EventInstance {
	private EventType event;
	private EventInstanceContainer cft;
	private String guid;
	private int id;
	private String author;
	private String description;
	
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getAuthor() {
		return author;
	}

	@Override
	public EventType getEvent() {
		return event;
	}

	@Override
	public EventInstanceContainer getContainer() {
		return cft;
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
	public String toString() {
		return "EventInstance of: "+event.getName()+";  CFT: "+cft.getName()+";  ID: "+id+";  guid: "+guid + "; author: " + author + "; description: " + description;
	}

	public EventInstanceImpl(EventType event, EventInstanceContainer cft, String author, String description, String guid, int id) {
		this.event = event;
		this.cft = cft;
		this.author = author;
		this.description = description;
		this.guid = guid;
		this.id = id;
		
		event.addInstance(this);
		cft.addEvent(this);
	}
}