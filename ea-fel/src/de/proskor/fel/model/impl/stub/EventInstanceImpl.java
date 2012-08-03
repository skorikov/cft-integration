package de.proskor.fel.model.impl.stub;

import de.proskor.fel.model.container.EventInstanceContainer;
import de.proskor.fel.model.event.EventInstance;
import de.proskor.fel.model.event.EventType;

public class EventInstanceImpl extends EntityImpl implements EventInstance {
	private EventInstanceContainer container;
	private EventType typ;

	@Override
	public EventInstanceContainer getContainer() {
		return this.container;
	}

	public void setContainer(EventInstanceContainer container) {
		this.container = container;
	}

	@Override
	public EventType getType() {
		return this.typ;
	}

	@Override
	public void setType(EventType typ) {
		this.typ = typ;
	}	
	
	@Override
	public String getQualifiedName() {
		return getContainer().getQualifiedName() + "." + getName();
	}
}
