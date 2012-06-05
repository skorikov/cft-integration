package de.proskor.ui.failureEventList;


public class EventInstance {
	private Event event;
	private EventCFT cft;
	private String guid;
	private int id;
	private String author;
	private String description;
	
	public String getDescription() {
		return description;
	}

	public String getAuthor() {
		return author;
	}

	public Event getEvent() {
		return event;
	}

	public EventCFT getCft() {
		return cft;
	}

	public String getGuid() {
		return guid;
	}

	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "EventInstance of: "+event.getName()+";  CFT: "+cft.getName()+";  ID: "+id+";  guid: "+guid + "; author: " + author + "; description: " + description;
	}

	public EventInstance(Event event, EventCFT cft, String author, String description, String guid, int id) {
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