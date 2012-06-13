package de.proskor.automation;

public final class MenuState {
	private final boolean enabled;
	private final boolean checked;

	public MenuState(final boolean enabled, final boolean checked) {
		this.enabled = enabled;
		this.checked = checked;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public boolean isChecked() {
		return this.checked;
	}
}
