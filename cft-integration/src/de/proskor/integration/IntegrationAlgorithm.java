package de.proskor.integration;

import de.proskor.integration.model.Component;
import de.proskor.integration.model.impl.ComponentImpl;

public class IntegrationAlgorithm {
	public Component integrate(Component left, Component right) {
		final Component result = this.copy(left);
		this.merge(result, right);
		this.deleteRedundancy(result);
		this.checkAnamoly(result);
		return result;
	}

	private Component copy(Component component) {
		return new ComponentImpl();
	}

	private void merge(Component left, Component right) {
		final Component ivbcft = left.find(right);
		ivbcft.getInports().addAll(right.getInports());
		ivbcft.getOutports().addAll(right.getOutports());
		ivbcft.getBasicEvents().addAll(right.getBasicEvents());
		this.addMissingLogic(ivbcft, right);
		for (final Component kid : right.getComponents()) {
			this.merge(left, kid);
		}
	}

	private void addMissingLogic(Component left, Component right) {
		
	}

	private void deleteRedundancy(Component component) {
	}

	private void checkAnamoly(Component component) {
	}
}
