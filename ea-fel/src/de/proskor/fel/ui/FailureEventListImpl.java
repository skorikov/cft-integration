package de.proskor.fel.ui;

import de.proskor.fel.*;
import de.proskor.fel.container.*;
import de.proskor.fel.event.*;
import de.proskor.fel.impl.stub.*;

public class FailureEventListImpl implements FailureEventList {
	private FailureEventListGui felGui;
	private final EventRepository eventRepository;
	
	public FailureEventListImpl(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
		this.felGui = new FailureEventListGui(this);
	}
	
	private static void setEntityStubData(Entity entity, String name) {
		EntityImpl entityImpl = (EntityImpl) entity;
		
		entityImpl.setName(name);
	}
	
	private static EventTypeContainer createDummyComponent(String name, EventTypeContainer parentContainer, int subContainerCount, int subContainerDepth, int eventsCount) {
		EventTypeContainer typeContainer;
		
		if (parentContainer != null)
			typeContainer = new EventTypeContainerImpl(parentContainer);
		else
			typeContainer = new EventTypeContainerImpl();
			
		typeContainer.setAuthor("@Author-Generated");
		typeContainer.setDescription("@Description-Generated");
		setEntityStubData(typeContainer, name);
		
		for(int i=0; i<eventsCount; i++) { 
			EventType event = typeContainer.createEventType(name+".event." + i);
			event.setAuthor("@Author-Generated["+i+"]");
			event.setDescription("@Description-Generated["+i+"]");
		}
		
		if (subContainerDepth > 0) {
			for(int i=0; i<subContainerCount; i++) {
				EventTypeContainer subContainer = createDummyComponent(
						name+"."+i, typeContainer, subContainerCount, subContainerDepth-1, eventsCount
					);
			}
		}
		
		return typeContainer;
	}
	
	public static void main(String[] args) {
		System.out.println("App started.");

		EventRepository rep = new EventRepositoryImpl();

		for(int i=0; i<5; i++) {
			EventTypeContainer dummyContainer = createDummyComponent("component ["+i+"]", null, 4, 3, 5);
			rep.getEventTypeContainers().add(dummyContainer);
		}
		
		
		FailureEventListImpl fel = new FailureEventListImpl(rep);
		fel.showDialog();
		
		System.out.println("FailureEventList created.");
	}

	@Override
	public void showDialog() {
		felGui.show();
	}

	@Override
	public void showDialog(EventTypeContainer selectedContainer) {
		felGui.show(selectedContainer);
	}

	@Override
	public void showDialog(EventType selectedEvent) {
		felGui.show(selectedEvent);
	}

	@Override
	public EventRepository getRepository() {
		return eventRepository;
	}
}
