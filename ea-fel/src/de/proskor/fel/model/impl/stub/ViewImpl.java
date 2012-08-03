package de.proskor.fel.model.impl.stub;

import de.proskor.fel.model.view.View;

public abstract class ViewImpl implements View {
	private String name;

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
