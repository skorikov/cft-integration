package de.proskor.automation;

import cli.EA.IRepository;

/**
 * A dummy implementation of the AddIn interface.
 * Clients should refine this class and override the required methods.
 */
public abstract class AddInAdapter implements AddIn {
	/**
	 * The menu is enabled and not checked by default.
	 */
	private static final MenuState DEFAULT_MENU_STATE = new MenuState(true, false);

	/** NOP */
	@Override
	public void start() {}

	/** NOP */
	@Override
	public void stop() {}

	/** NOP */
	@Override
	public void initialize(IRepository repository) {}

	/**
	 * Return always a new array to prevent aliasing. 
	 */
	@Override
	public String[] getMenuItems(IRepository repository, String location, String menu) {
		return new String[0];
	}

	/**
	 * Return default menu state. 
	 */
	@Override
	public MenuState getMenuState(IRepository repository, String location, String menu, String item) {
		return AddInAdapter.DEFAULT_MENU_STATE;
	}

	/** NOP */
	@Override
	public void menuItemClicked(IRepository repository, String location, String menu, String item) {}
}
