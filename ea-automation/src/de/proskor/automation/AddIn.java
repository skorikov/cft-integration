package de.proskor.automation;

import cli.EA.IRepository;

/**
 * Simple abstraction of the AI interface. AddIns need to implement this (refine AddInAdapter).
 * The .NET part is implemented in C# and forwards the EA events to the AddIn implementation.
 */
public interface AddIn {
	/**
	 * Called during initialization of EA.
	 * AI Event: EA_Connect.
	 * Since the repository is useless at this moment, don't forward it. 
	 */
	public void start();

	/**
	 * Called when EA is closing.
	 * AI Event: EA_Disconnect.
	 */
	public void stop();

	/**
	 * Called after the repository became initialized.
	 * AI Event: EA_OnPostInitialized.
	 * @param repository passed by EA.
	 */
	public void initialize(IRepository repository);

	/**
	 * Get the menu items.
	 * AI Event: EA_GetMenuItems.
	 * @param repository passed by EA.
	 * @param location passed by EA.
	 * @param menu passed by EA.
	 * @return always an array of strings.
	 * Single items or empty menus are wrapped in a (possibly empty) array.
	 */
	public String[] getMenuItems(IRepository repository, String location, String menu);

	/**
	 * Get menu state.
	 * AI Event: EA_Get_MenuState.
	 * @param repository passed by EA.
	 * @param location passed by EA.
	 * @param menu passed by EA.
	 * @param item passed by EA.
	 * @return menu state. Set the referenced arguments according to this object.
	 */
	public MenuState getMenuState(IRepository repository, String location, String menu, String item);

	/**
	 * Called when a menu item is clicked.
	 * AI Event: EA_MenuClick.
	 * Invoke the corresponding action here.
	 * @param repository passed by EA.
	 * @param menu passed by EA.
	 * @param item passed by EA.
	 */
	public void menuItemClicked(IRepository repository, String menu, String item);
}
