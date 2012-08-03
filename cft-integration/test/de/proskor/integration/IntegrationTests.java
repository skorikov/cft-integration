package de.proskor.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import de.proskor.integration.model.Component;
import de.proskor.integration.model.impl.BasicEventImpl;
import de.proskor.integration.model.impl.ComponentImpl;

public class IntegrationTests {
	/** Merge empty components. */
	@Test
	public void testComponents() {
		final String name = "C1";
		final Component left = new ComponentImpl(name);
		final Component right = new ComponentImpl(name);
		final IntegrationAlgorithm algorithm = new IntegrationAlgorithm();
		final Component result = algorithm.integrate(left, right);
		assertNotNull(result);
		assertEquals(name, result.getName());
	}

	/** Merge components with basic events. */
	@Test
	public void testComponentsWithEvents() {
		final String name = "C1";
		final Component left = new ComponentImpl(name);
		left.getBasicEvents().add(new BasicEventImpl("B1"));
		final Component right = new ComponentImpl(name);
		right.getBasicEvents().add(new BasicEventImpl("B2"));
		final IntegrationAlgorithm algorithm = new IntegrationAlgorithm();
		final Component result = algorithm.integrate(left, right);
		assertEquals(2, result.getBasicEvents().size());
	}
}
