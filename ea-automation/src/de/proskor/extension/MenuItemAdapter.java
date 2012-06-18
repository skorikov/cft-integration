package de.proskor.extension;

import java.util.List;
import java.util.ArrayList;

/**
 * A dummy implementation for the menu item interface.
 * Clients should subclass this.
 */
public class MenuItemAdapter implements MenuItem {
	private String name;
	private boolean visible = true;
	private boolean enabled = true;
	private boolean checked = false;
	private List<MenuItem> kids = new ArrayList<MenuItem>();

	public MenuItemAdapter(String name) {
		this.name = name;
	}

	public MenuItemAdapter(MenuItem parent, String name) {
		this(name);
		parent.getChildren().add(this);
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean hasChildren() {
		return !this.kids.isEmpty();
	}

	@Override
	public List<MenuItem> getChildren() {
		return this.kids;
	}

	@Override
	public boolean isVisible() {
		return this.visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isChecked() {
		return this.checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	/** NOP */
	@Override
	public void invoke() {}

	@Override
	public String toString() {
		return name + "(visible=" + this.visible + ",enabled=" + this.enabled + ",checked=" + this.checked + ",hasChildren=" + this.hasChildren() + ")";
	}
}
