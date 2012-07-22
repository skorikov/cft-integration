package de.proskor.extension;

import java.util.List;

/**
 * Menu Item interface.
 */
public interface MenuItem extends Runnable {
	/**
	 * Get the name of this menu item.
	 * @return name.
	 */
	public String getName();

	/**
	 * Determine if the item has a sub-menu.
	 * @return true if it has children, false otherwise.
	 */
	public boolean hasChildren();

	/**
	 * Get sub-items of this menu item.
	 * The result collection is read-only.
	 * @return list of child items.
	 */
	public List<MenuItem> getChildren();

	/**
	 * Determine if the menu item should be displayed.
	 * @return true if the item is visible, false otherwise.
	 */
	public boolean isVisible();

	/**
	 * Get the enable status of the item.
	 * @return true if this menu item is enabled, false otherwise.
	 */
	public boolean isEnabled();

	/**
	 * Get the checked status of the item.
	 * @return true if this menu item is checked, false otherwise.
	 */
	public boolean isChecked();
}
