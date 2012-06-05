package de.proskor.fel.impl;

import java.util.ArrayList;


public class Event {
	private String name;
	private String guid;
	private int id;
	private String author;
	private String description;

	private ArrayList<EventInstance> instances;

	public String getName() {
		return name;
	}

	public String getGuid() {
		return guid;
	}
	
	public int getId() {
		return id;
	}

	public String getAuthor() {
		return author;
	}

	public String getDescription() {
		return description;
	}

	public ArrayList<EventInstance> getInstances() {
		return instances;
	}
	
	protected void addInstance(EventInstance eventInstance) {
		instances.add(eventInstance);
	}

	@Override
	public String toString() {
		return "Event: "+name+";  ID: "+id+";  guid: "+guid + "; author: " + author + "; description: " + description;
	}
	
	public Event(String name, String author, String description, String guid, int id) {
		this.name = name;
		this.author = author;
		this.description = description;
		this.guid = guid;
		this.id = id;

		instances = new ArrayList<EventInstance>();
	}
}