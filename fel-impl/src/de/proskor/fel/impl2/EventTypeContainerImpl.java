package de.proskor.fel.impl2;

import java.util.List;

import de.proskor.fel.container.EventInstanceContainer;
import de.proskor.fel.container.EventTypeContainer;
import de.proskor.fel.event.EventType;
import de.proskor.model.Element;

public class EventTypeContainerImpl implements EventTypeContainer {
	private final Element peer;

	public EventTypeContainerImpl(Element peer) {
		this.peer = peer;
	}

	@Override
	public String getName() {
		return this.peer.getName();
	}

	@Override
	public int getId() {
		return this.peer.getId();
	}

	@Override
	public String getGuid() {
		return this.peer.getGuid();
	}

	@Override
	public String getDescription() {
		return this.peer.getDescription();
	}

	@Override
	public String getAuthor() {
		return this.peer.getAuthor();
	}

	@Override
	public void setDescription(String description) {
		this.peer.setDescription(description);
	}

	@Override
	public void setAuthor(String author) {
		this.peer.setAuthor(author);
	}

	@Override
	public String getQualifiedName() {
		// TODO Auto-generated method stub
		return "TODO";
	}

	@Override
	public List<EventType> getEvents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EventInstanceContainer> getInstances() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventType createEventType(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addInstance(EventInstanceContainer instance) {
		// TODO Auto-generated method stub

	}

	@Override
	public EventTypeContainer getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EventTypeContainer> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}
}
