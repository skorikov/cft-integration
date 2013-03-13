package de.proskor.cft.merge.ui;

import de.proskor.cft.Component;

final class ComponentWrapper extends ElementWrapper {
	private final Component component;

	public ComponentWrapper(Component component) {
		super(component);
		this.component = component;
	}

	public Component getComponent() {
		return this.component;
	}
}
