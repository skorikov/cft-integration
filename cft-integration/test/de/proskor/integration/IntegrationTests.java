package de.proskor.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

import de.proskor.integration.model.BasicEvent;
import de.proskor.integration.model.Component;
import de.proskor.integration.model.EventType;
import de.proskor.integration.model.impl.BasicEventImpl;
import de.proskor.integration.model.impl.ComponentImpl;
import de.proskor.integration.model.impl.EventTypeImpl;

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
		final BasicEvent lbe = new BasicEventImpl("B1");
		lbe.setType(new EventTypeImpl("T1"));
		left.getBasicEvents().add(lbe);
		final Component right = new ComponentImpl(name);
		final BasicEvent rbe = new BasicEventImpl("B2");
		rbe.setType(new EventTypeImpl("T2"));
		right.getBasicEvents().add(rbe);
		final IntegrationAlgorithm algorithm = new IntegrationAlgorithm();
		final Component result = algorithm.integrate(left, right);
		assertEquals(2, result.getBasicEvents().size());
	}

	/** Merge components with common events. */
	@Ignore
	@Test
	public void testComponentsWithCommonEvents() {
		final String name = "C1";
		final EventType type = new EventTypeImpl("T1");
		final Component left = new ComponentImpl(name);
		final BasicEvent lbe = new BasicEventImpl("B1");
		lbe.setType(type);
		left.getBasicEvents().add(lbe);
		final Component right = new ComponentImpl(name);
		final BasicEvent rbe = new BasicEventImpl("B2");
		rbe.setType(type);
		right.getBasicEvents().add(rbe);
		final IntegrationAlgorithm algorithm = new IntegrationAlgorithm();
		final Component result = algorithm.integrate(left, right);
		assertEquals(1, result.getBasicEvents().size());
	}
}
