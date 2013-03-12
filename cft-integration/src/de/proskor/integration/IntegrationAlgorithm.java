package de.proskor.integration;

import java.util.Collection;

import de.proskor.integration.model.BasicEvent;
import de.proskor.integration.model.Component;
import de.proskor.integration.model.EventType;
import de.proskor.integration.model.impl.BasicEventImpl;
import de.proskor.integration.model.impl.ComponentImpl;

public class IntegrationAlgorithm extends MappingContext {
	public Component integrate(Component left, Component right) {
		final Component result = this.copy(left);
		this.merge(result, right);
	//	this.deleteRedundancy(result);
	//	this.checkAnamoly(result);
		return result;
	}

	private Component copy(Component component) {
		final Component result = new ComponentImpl(component.getName());
		final Collection<BasicEvent> events = result.getBasicEvents();
		for (final BasicEvent event : component.getBasicEvents()) {
			final BasicEvent copy = new BasicEventImpl(event.getName());
			events.add(copy);
			this.put(event, copy);
		}
		return result;
	}

	private void merge(Component left, Component right) {
		final Component ivbcft = left.find(right);

		final Collection<BasicEvent> events = left.getBasicEvents();
		for (final BasicEvent event : right.getBasicEvents()) {
			if (!this.containsEvent(left, event)) {
				final BasicEvent copy = new BasicEventImpl(event.getName());
				events.add(copy);
				this.put(event, copy);
			}
		}
	/*	for (final Inport inport : right.getInports()) {
			
		}

		ivbcft.getInports().addAll(right.getInports());
		ivbcft.getOutports().addAll(right.getOutports());
		ivbcft.getBasicEvents().addAll(right.getBasicEvents());*/
		this.addMissingLogic(ivbcft, right);
	/*	for (final Component kid : right.getComponents()) {
			this.merge(left, kid);
		}*/
	}

	private boolean containsEvent(Component component, BasicEvent event) {
		for (final BasicEvent kid : component.getBasicEvents()) {
			if (kid.getName().equals(event.getName()))
				return true;
		}
		return false;
	}

	private void addMissingLogic(Component left, Component right) {
		
	}

	private void deleteRedundancy(Component component) {
	}

	private void checkAnamoly(Component component) {
	}

	private Component merge2(Component left, Component right) throws MergeException {
		final String name = left.getName();

		final Component result = new ComponentImpl();
		result.setName(name);
	
		return result;
	}

	private BasicEvent merge(BasicEvent left, BasicEvent right) throws MergeException {
		final String name = left.getName();
		if (!right.getName().equals(name))
			throw new MergeException();

		final EventType type = left.getType();
		if (!right.getType().equals(type))
			throw new MergeException();

		final BasicEvent result = new BasicEventImpl();
		result.setName(name);
		result.setType(type);

		return result;
	}
}
