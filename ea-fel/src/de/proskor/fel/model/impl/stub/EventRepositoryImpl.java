package de.proskor.fel.model.impl.stub;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.proskor.fel.model.EventRepository;
import de.proskor.fel.model.container.EventTypeContainer;
import de.proskor.fel.model.view.ArchitecturalView;
import de.proskor.fel.model.view.ComponentFaultTree;
import de.proskor.fel.model.view.View;
import de.proskor.fel.model.view.ViewRepository;

public class EventRepositoryImpl implements EventRepository, ViewRepository {
	private List<EventTypeContainer> containers = new ArrayList<EventTypeContainer>();
	private List<ArchitecturalView> archViews = new ArrayList<ArchitecturalView>();
	private List<ComponentFaultTree> cfts = new ArrayList<ComponentFaultTree>();

	@Override
	public List<EventTypeContainer> getEventTypeContainers() {
		return this.containers;
	}

	@Override
	public List<? extends View> getViews() {
		List<View> result = new LinkedList<View>(this.archViews);
		result.addAll(this.cfts);
		return result;
	}
}
