package de.proskor.fel.impl;

import de.proskor.fel.container.EventInstanceContainer;
import de.proskor.fel.event.EventInstance;
import de.proskor.fel.event.EventType;


public class EventInstanceImpl extends EntityImpl implements EventInstance {
	private final EventType event;
	private EventInstanceContainer instanceContainer;
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
	public EventType getType() {
		return event;
	}

	@Override
	public EventInstanceContainer getContainer() {
		return instanceContainer;
	}
	
	@Override
	public String toString() {
		return "EventInstance of: "+event.getName()+";  Container: "+instanceContainer.getName()+";  ID: "+getId()+";  guid: "+getGuid()+ "; author: " + author + "; description: " + description;
	}

	public EventInstanceImpl(EventType event, EventInstanceContainer instanceContainer, String author, String description, String guid, int id) {
		super("", guid, id); // Der Name einer Event-Instance entspricht dem des Event-Typs.
		
		this.event = event;
		this.instanceContainer = instanceContainer;
		this.author = author;
		this.description = description;
		
		event.addInstance(this);
		instanceContainer.addEvent(this);
	}

	/**
	 * Every Event has the name of its {@link EventType}.
	 */
	@Override
	public String getName() {
		return getType().getName();
	}
}