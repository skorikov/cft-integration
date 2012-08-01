package de.proskor;

import de.proskor.extension.ExtensionAdapter;
import de.proskor.model.Repository;
import de.proskor.model.RepositoryProvider;

public abstract class ExtensionWithTest extends ExtensionAdapter {
	@Override
	public void initialize(Repository repository) {
		super.initialize(repository);
		RepositoryProvider.setRepository(repository);
	}
}
