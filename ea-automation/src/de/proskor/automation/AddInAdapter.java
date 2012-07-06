package de.proskor.automation;

import cli.EA.IEventProperties;
import cli.EA.IRepository;

/**
 * A dummy implementation of the AddIn interface.
 * Clients should refine this class and override the required methods.
 */
public abstract class AddInAdapter implements AddIn {
	/** The menu is enabled and not checked by default */
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

	/** Always return a new array to prevent aliasing */
	@Override
	public String[] getMenuItems(IRepository repository, String location, String menu) {
		return new String[0];
	}

	/** Return default menu state */
	@Override
	public MenuState getMenuState(IRepository repository, String location, String menu, String item) {
		return AddInAdapter.DEFAULT_MENU_STATE;
	}

	/** NOP */
	@Override
	public void menuItemClicked(IRepository repository, String location, String menu, String item) {}

	/** Permit deletion of elements by default */
	@Override
	public boolean deleteElement(IRepository repository, IEventProperties properties) {
		return true;
	}

	/** Permit deletion of packages by default */
	@Override
	public boolean deletePackage(IRepository repository, IEventProperties properties) {
		return true;
	}

	/** Permit deletion of diagrams by default */
	@Override
	public boolean deleteDiagram(IRepository repository, IEventProperties properties) {
		return true;
	}
}
