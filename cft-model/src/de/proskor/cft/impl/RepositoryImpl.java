package de.proskor.cft.impl;

import java.util.Collection;
import java.util.LinkedList;

import de.proskor.cft.Package;
import de.proskor.cft.Repository;

public class RepositoryImpl implements Repository {
	private final de.proskor.model.Repository peer;

	public RepositoryImpl(de.proskor.model.Repository peer) {
		if (peer == null)
			throw new IllegalArgumentException("peer is null");

		this.peer = peer;
	}

	@Override
	public Collection<Package> getPackages() {
		final Collection<Package> packages = new LinkedList<Package>();
		for (final de.proskor.model.Package kid : this.peer.getModels()) {
			packages.add(new PackageImpl(kid));
		}
		return packages;
	}
}
