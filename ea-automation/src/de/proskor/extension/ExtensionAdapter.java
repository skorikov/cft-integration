package de.proskor.extension;

/**
 * A dummy implementation of the Extension interface.
 * Clients should refine this class and override the required methods.
 */
public abstract class ExtensionAdapter implements Extension {
	/** Stores the menu. */
	private MenuItem menu;

	/**
	 * Constructor.
	 * Calls createMenus() to initialize the menu structure.
	 */
	protected ExtensionAdapter() {
		this.menu = this.createMenu();
	}

	/**
	 * Create the menu.
	 * This method is called when the extension is initialized.
	 * Clients should override this method.
	 */
	protected abstract MenuItem createMenu();

	/** NOP */
	@Override
	public void start() {}

	/** NOP */
	@Override
	public void stop() {}

	/**
	 * Get the menu.
	 */
	@Override
	public final MenuItem getMenu() {
		return this.menu;
	}
}
