package de.proskor;

import de.proskor.extension.ExtensionAdapter;
import de.proskor.extension.MenuItem;
import de.proskor.extension.MenuItemAdapter;

public class CftExtension extends ExtensionAdapter {
	@Override
	protected MenuItem createMenu() {
		final MenuItemAdapter main = new MenuItemAdapter("CFT");
		new MenuItemAdapter(main, "Test") {
			@Override
			public void run() {
				CftExtension.this.getRepository().getOutputTab("Test").write("hello world");
			}
		};
		return main;
	}
}
