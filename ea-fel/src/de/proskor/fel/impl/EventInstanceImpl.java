package de.proskor.fel.impl;

import de.proskor.fel.Event;
import de.proskor.fel.EventCFT;
import de.proskor.fel.EventInstance;


public class EventInstanceImpl implements EventInstance {
	private Event event;
	private EventCFT cft;
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
	public Event getEvent() {
		return event;
	}

	@Override
	public EventCFT getCft() {
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

	public EventInstanceImpl(Event event, EventCFT cft, String author, String description, String guid, int id) {
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