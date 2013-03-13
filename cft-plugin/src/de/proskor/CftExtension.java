package de.proskor;

import de.proskor.cft.Component;
import de.proskor.cft.Package;
import de.proskor.cft.Repository;
import de.proskor.cft.impl.RepositoryImpl;
import de.proskor.cft.merge.ui.MergeDialog;
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
				final Repository repository = CftExtension.this.getCftRepository();
			/*	for (final Package pkg : repository.getPackages()) {
					final Component component = pkg.createComponent();
					component.setName("new component");
				}*/
				this.printContents(repository);
			}

			private void printContents(Repository repository) {
				for (final Package pkg : repository.getPackages()) {
					this.print(pkg.getName());
					printContents(pkg, 1);
				}
			}

			private void printContents(Package pkg, int level) {
				String prefix = "";
				for (int i = 0; i < level; i++) {
					prefix += ":";
				}
				prefix += " ";
				for (final Package kid : pkg.getPackages()) {
					this.print(prefix + "PKG " + kid.getName());
					printContents(kid, level + 1);
				}
				for (final Component kid : pkg.getComponents()) {
					this.print(prefix + "CMP " + kid.getName());
				}
			}

			private void print(String text) {
				CftExtension.this.getRepository().getOutputTab("Test").write(text);
			}
		};

		new MenuItemAdapter(main, "Merge") {
			@Override
			public void run() {
				new MergeDialog().show(CftExtension.this.getCftRepository());
			}
		};

		return main;
	}

	private Repository getCftRepository() {
		return new RepositoryImpl(this.getRepository());
	}
}
