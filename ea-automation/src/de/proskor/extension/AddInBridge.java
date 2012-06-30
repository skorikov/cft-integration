package de.proskor.extension;

import java.util.LinkedList;
import java.util.List;

import cli.EA.IRepository;
import de.proskor.automation.AddInAdapter;
import de.proskor.automation.MenuState;

/**
 * Bridges the AddIn and the Extension interfaces.
 * Clients should refine this class and override the createExtension method.
 */
public abstract class AddInBridge extends AddInAdapter {
	/** Actual extension implementation */
	private Extension extension = null;

	/**
	 * Get the extension implementation.
	 * Clients should override this method and call setExtension() to configure the implementation.
	 * @return actual implementation.
	 */
	protected abstract Extension createExtension();

	/**
	 * Get the extension implementation.
	 * @return extension implementation.
	 */
	private Extension getExtension() {
		if (this.extension == null)
			this.extension = this.createExtension();

		return this.extension;
	}

	/**
	 * Start the extension.
	 */
	@Override
	public final void start() {
		this.getExtension().start();
	}

	/**
	 * Stop the extension.
	 */
	@Override
	public final void stop() {
		this.getExtension().stop();
	}

	/**
	 * Get the menu structure.
	 */
	@Override
	public final String[] getMenuItems(IRepository repository, String location, String menu) {
		final MenuItem topMenuItem = this.getMenu();

		if (menu.equals("")) {
			if (topMenuItem == null)
				return new String[0];
			else
				return new String[] { this.formatMenuItem(topMenuItem) };
		}

		if (!menu.startsWith("-"))
			throw new IllegalArgumentException("Requested children from an item with no children: '" + menu + "'.");

		final String menuItemName = menu.substring(1);
		final MenuItem menuItem = this.findMenuItem(topMenuItem, menuItemName);

		if (!menuItem.hasChildren())
			throw new IllegalArgumentException("Menu Item '" + menu + "' has no children.");

		final List<String> menuItems = new LinkedList<String>();
		for (final MenuItem kid : menuItem.getChildren()) {
			if (kid.isVisible())
				menuItems.add(this.formatMenuItem(kid));
		}

		return menuItems.toArray(new String[0]);
	}

	/**
	 * Get menu items state.
	 */
	@Override
	public final MenuState getMenuState(IRepository repository, String location, String menu, String item) {
		final MenuItem topMenuItem = this.getMenu();
		final MenuItem menuItem = this.findMenuItem(topMenuItem, menu, item);
		return new MenuState(menuItem.isEnabled(), menuItem.isChecked());
	}

	/**
	 * Perform action on click.
	 */
	@Override
	public final void menuItemClicked(IRepository repository, String location, String menu, String item) {
		final MenuItem topMenuItem = this.getMenu();
		final MenuItem menuItem = this.findMenuItem(topMenuItem, menu, item);

		menuItem.invoke();
	}

	/**
	 * Get the top menu item.
	 * @return top-level menu item or null if none is defined.
	 */
	private MenuItem getMenu() {
		return this.getExtension().getMenu();
	}

	/**
	 * Format menu item.
	 * Prepend "-" if the menu item has children.
	 * @param menuItem menu item to format.
	 * @return formatted menu item string.
	 */
	private String formatMenuItem(MenuItem menuItem) {
		final String itemName = menuItem.getName();
		return menuItem.hasChildren() ? "-" + itemName : itemName;
	}

	/**
	 * Find contained menu item.
	 * @param context top menu item.
	 * @param menu containing item name.
	 * @param item item name.
	 * @return the contained menu item.
	 */
	private MenuItem findMenuItem(MenuItem context, String menu, String item) {
		if (!menu.startsWith("-"))
			throw new IllegalArgumentException("Requested children from an item with no children: '" + menu + "'.");

		final String containingItemName = menu.substring(1);
		final MenuItem containingItem = this.findMenuItem(context, containingItemName);

		final String itemName = item.startsWith("-") ? item.substring(1) : item;
		return this.findMenuItem(containingItem, itemName);
	}

	/**
	 * Recursively find a child item.
	 * @param item containing menu item.
	 * @param name name of the item.
	 * @return child item.
	 */
	private MenuItem findMenuItem(MenuItem item, String name) {
		if (item.getName().equals(name))
			return item;
		else {
			for (final MenuItem kid : item.getChildren()) {
				final MenuItem childItem = this.findMenuItem(kid, name); 
				if (childItem != null)
					return childItem;
			}
			return null;
		}
	}
}
