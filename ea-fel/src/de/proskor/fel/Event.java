package de.proskor.fel;

import java.util.List;

public interface Event {
	public int getId();
	public String getGuid();
	public String getName();
	public String getAuthor();
	public String getDescription();
	public List<EventInstance> getInstances();
	public void addInstance(EventInstance eventInstance);
}