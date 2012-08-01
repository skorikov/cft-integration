package de.proskor.model.ext.impl;

import de.proskor.model.ext.Collection;
import de.proskor.model.ext.Package;
import de.proskor.model.ext.Repository;

public class RepositoryImpl implements Repository {
	private final de.proskor.model.Repository peer;

	public RepositoryImpl(de.proskor.model.Repository peer) {
		this.peer = peer;
	}

	@Override
	public Collection<Package> getModels() {
		return new CollectionImpl<Package, de.proskor.model.Package>(this.peer.getModels()) {
			@Override
			Package wrap(de.proskor.model.Package element) {
				return new PackageImpl(element);
			}

			@Override
			de.proskor.model.Package unwrap(Package element) {
				if (!(element instanceof PackageImpl))
					throw new IllegalArgumentException();

				return ((PackageImpl) element).getPeer();
			}
		};
	}

	@Override
	public Package createModel(String name) {
		final de.proskor.model.Package pkg = this.peer.getModels().add(name, de.proskor.model.Package.PACKAGE);
		return new PackageImpl(pkg);
	}

}
