package de.proskor.automation;

import cli.EA.IRepository;

public abstract class AddInAdapter implements AddIn {
	private final MenuState defaultMenuState = new MenuState(true, false);

	@Override
	public void start() {}

	@Override
	public void stop() {}

	@Override
	public void initialize(IRepository repository) {}

	@Override
	public String[] getMenuItems(IRepository repository, String location, String menu) {
		return new String[0];
	}

	@Override
	public MenuState getMenuState(IRepository repository, String location, String menu, String item) {
		return this.defaultMenuState;
	}

	@Override
	public void menuItemClicked(IRepository repository, String menu, String item) {}
}
