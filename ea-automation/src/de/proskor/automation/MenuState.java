package de.proskor.automation;

/**
 * Encapsulates the state of a menu item. 
 * This class is immutable and thread-safe.
 */
public final class MenuState {
	private final boolean enabled;
	private final boolean checked;

	/**
	 * Creates a new instance.
	 * @param enabled true if menu item is enabled.
	 * @param checked true if menu item is checked.
	 */
	public MenuState(boolean enabled, boolean checked) {
		this.enabled = enabled;
		this.checked = checked;
	}

	/**
	 * Get enabled state.
	 * @return true if menu item is enabled, false otherwise.
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * Get checked state.
	 * @return true if menu item is checked, false otherwise.
	 */
	public boolean isChecked() {
		return this.checked;
	}
}
