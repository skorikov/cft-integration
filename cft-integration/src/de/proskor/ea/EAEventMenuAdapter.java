package de.proskor.ea;

import cli.EA.IElement;
import cli.EA.IRepository;
import de.proskor.ea.model.EAElement;
import de.proskor.fel.FailureEventList;

public class EAEventMenuAdapter {
	private final FailureEventList fel;
	
	public EAEventMenuAdapter() {
		// fel = new FailureEventList(); // TODO: Ausführen, sobald EventRepository läuft.
		fel = null;
	}
	
	public EAElement getSelectedElement(IRepository repository) {
		Object obj = repository.GetContextObject();
		IElement eaElement = (IElement)obj;
		
		return new EAElement(eaElement, repository);
	}
	
	public boolean newEventTypeMenu_isEnabled(IRepository repository) {
		EAElement selectedElement = getSelectedElement(repository);
		return EAConstants.stereotypeComponent.equals(selectedElement.stereotype());
	}
	
	public boolean newEventInstanceMenu_isEnabled(IRepository repository) {
		EAElement selectedElement = getSelectedElement(repository);
		return EAConstants.stereotypeCFT.equals(selectedElement.stereotype());
	}
	
	//TODO: On-Click Ereignisse für einzelne EventInstance-Menu-Items fehlen noch.
//	public String[] EA_GetMenuItems_ForEventInstanceMenuItem(IRepository repository) {
//		fel.reloadRepository();
//		EAElement elem = getSelectedElement(repository);
//		
//		EventTypeContainer container = fel.getEventTypeContainerById(elem.id());
//		if (container != null) {
//			String[] names = new String[container.getEvents().size()];
//			
//			int i=0;
//			for(EventType event : container.getEvents()) {
//				names[i] = event.getName();
//				i++;
//			}
//			
//			return names;
//		}	
//			
//		return new String[] {"No Event-Types defined."};
//	}
	
	public void createEventType(IRepository repository) {
		fel.reloadRepository();
		fel.createEventType(); // User kann hier ein neues Element erzeugen.
	}
	
	public void createEventInstance(IRepository repository) {
		fel.reloadRepository();
		fel.createEventInstance(); // User kann hier ein neues Element erzeugen.	
	}
}
