package de.proskor.fel.impl;

import java.util.ArrayList;
import java.util.List;

import de.proskor.fel.Event;
import de.proskor.fel.EventInstance;


public class EventImpl implements Event {
	private String name;
	private String guid;
	private int id;
	private String author;
	private String description;

	private List<EventInstance> instances;

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
		instances.add(eventInstance);
	}

	@Override
	public String toString() {
		return "Event: "+name+";  ID: "+id+";  guid: "+guid + "; author: " + author + "; description: " + description;
	}
	
	public EventImpl(String name, String author, String description, String guid, int id) {
		this.name = name;
		this.author = author;
		this.description = description;
		this.guid = guid;
		this.id = id;
		instances = new ArrayList<EventInstance>();
	}
}