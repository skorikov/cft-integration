package de.proskor.integration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.proskor.integration.model.BasicEvent;
import de.proskor.integration.model.Component;
import de.proskor.integration.model.Source;
import de.proskor.integration.model.Target;
import de.proskor.integration.model.impl.BasicEventImpl;
import de.proskor.integration.model.impl.ComponentImpl;

public class IntegrationAlgorithm {
	private Map<Source, Source> sources = new HashMap<Source, Source>();
	private Map<Target, Target> targets = new HashMap<Target, Target>();

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
}
