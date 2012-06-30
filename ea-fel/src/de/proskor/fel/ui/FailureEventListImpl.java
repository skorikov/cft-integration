package de.proskor.fel.ui;

import de.proskor.fel.*;
import de.proskor.fel.container.*;
import de.proskor.fel.event.*;
import de.proskor.fel.stub.*;

public class FailureEventListImpl implements FailureEventList {
	private FailureEventListGui felGui;
	private final EventRepository eventRepository;
	
	public FailureEventListImpl(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
		this.felGui = new FailureEventListGui(this);
	}
	
//	public EventTypeContainer getEventTypeContainerByGuid(String guid) {
//		for(EventTypeContainer container : eventRepository.getEventTypeContainers()) 
//			if (container.getGuid().equals(guid))
//				return container;
//		
//		return null;
//	}	
//	public EventTypeContainer getEventTypeContainerById(int id) {
//		for(EventTypeContainer container : eventRepository.getEventTypeContainers()) 
//			if (container.getId() == id)
//				return container;
//		
//		return null;
//	}
	
//	/**
//	 * Create {@link EventInstanceContainerImpl EventCFTs} containing the {@link EventInstanceImpl EventInstances}
//	 * and add them here. Call {@link #showEventList()} to open the GUI and get the <i>Events</i>
//	 * and <i>EventInstances</i> created by the User (if any). 
//	 * @param eventCft
//	 */
//	public void addEventCFT(EventInstanceContainer eventCft) {
//		felGuiHandler.addEventCFT(eventCft);
//	}
	
	private static void setEntityStubData(Entity entity, String name) {
		EntityImpl entityImpl = (EntityImpl) entity;
		
		entityImpl.setName(name);
	}
	
	private static EventTypeContainer createDummyComponent(String name, EventTypeContainer parentContainer, int containerInstancesCount, int subContainerCount, int subContainerDepth, int eventsCount, int eventInstancesCount) {
		EventTypeContainer typeContainer;
		
		if (parentContainer != null)
			typeContainer = new EventTypeContainerImpl(parentContainer);
		else
			typeContainer = new EventTypeContainerImpl();
			
		typeContainer.setAuthor("@Author-Generated");
		typeContainer.setDescription("@Description-Generated");
		setEntityStubData(typeContainer, name);
		
		EventInstanceContainer[] instanceContainer = new EventInstanceContainer[containerInstancesCount];
		
		for(int i=0; i<containerInstancesCount; i++) {
			instanceContainer[i] = new EventInstanceContainerImpl();
			((EventInstanceContainerImpl)instanceContainer[i]).setName(name + " Instance ["+i+"]");
			typeContainer.addInstance(instanceContainer[i]);

			for(int k=0; k<eventInstancesCount; k++) { 
				EventType event = typeContainer.createEvent(name+".event." + k);
				event.setAuthor("@Author-Generated");
				event.setDescription("@Description-Generated");
				
				EventInstance evInst = instanceContainer[i].createEvent("@Instance");
				evInst.setAuthor("@Author-Generated");
				evInst.setDescription("@Description-Generated");
				event.addInstance(evInst);
			}
		}
		
		if (subContainerDepth > 0) {
			for(int i=0; i<subContainerCount; i++) {
				EventTypeContainer subContainer = createDummyComponent(
						name+"."+i, typeContainer, containerInstancesCount, subContainerCount, subContainerDepth-1, eventsCount, eventInstancesCount
					);
			}
		}
		
		return typeContainer;
	}
	
	public static void main(String[] args) {
		System.out.println("App started.");

		EventRepository rep = new EventRepositoryImpl();

		for(int i=0; i<5; i++) {
			EventTypeContainer dummyContainer = createDummyComponent("Dummy["+i+"]", null, 3, 4, 3, 5, 10);
			rep.getEventTypeContainers().add(dummyContainer);
		}
		
		
		EventTypeContainer typeContainer = new EventTypeContainerImpl();
		setEntityStubData(typeContainer, "component1");
		typeContainer.setAuthor("comp1.author");
		typeContainer.setDescription("comp1.description");
		rep.getEventTypeContainers().add(typeContainer);

		EventType eventCommon = typeContainer.createEvent("common event");
		eventCommon.setAuthor("Yoda");
		eventCommon.setDescription("The force is strong.");
		
		EventInstanceContainer[] instanceContainer = new EventInstanceContainerImpl[4];
		
		for(int i=0; i<instanceContainer.length; i++) {
			instanceContainer[i] = new EventInstanceContainerImpl();
			((EventInstanceContainerImpl)instanceContainer[i]).setName("CFT" + i);
			typeContainer.addInstance(instanceContainer[i]);

			for(int k=0; k<2; k++) { 
				EventType event = typeContainer.createEvent("Event " + i+"."+k);
				event.setAuthor("Yoda");
				event.setDescription("The force is strong.");
				
				EventInstance evInst = instanceContainer[i].createEvent("<ev.inst>");
				evInst.setAuthor("Darth Vader");
				evInst.setDescription("The force is strong.");
				event.addInstance(evInst);

				System.out.println("- " + evInst);
			}
		}
		
		for(int i=0; i<instanceContainer.length; i++) { 
			EventInstance evInst = instanceContainer[i].createEvent("<common.inst>");

			evInst.setAuthor("Darth Vader");
			evInst.setDescription("Anything");
			eventCommon.addInstance(evInst);

			System.out.println("- " + evInst);
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
	public void showDialog(EventTypeContainer currentContainer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showDialog(EventType currentEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public EventRepository getRepository() {
		return eventRepository;
	}
}
