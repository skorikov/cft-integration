package de.proskor.extension;

/**
 * Menu Provider interface.
 * Defines the menu structure of the extension.
 */
public interface MenuProvider {
	/**
	 * Get Menu.
	 * @return top-level menu item or null if no menu is provided.
	 */
	public MenuItem getMenu();
}
