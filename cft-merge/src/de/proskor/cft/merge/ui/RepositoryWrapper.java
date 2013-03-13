package de.proskor.cft.merge.ui;

import java.util.Collection;
import java.util.LinkedList;

import de.proskor.cft.Component;
import de.proskor.cft.Package;
import de.proskor.cft.Repository;

final class RepositoryWrapper {
	private final Repository repository;

	public RepositoryWrapper(Repository repository) {
		if (repository == null)
			throw new IllegalArgumentException("repository is null");

		this.repository = repository;
	}

	public Collection<PackageWrapper> getPackages() {
		final Collection<PackageWrapper> packages = new LinkedList<PackageWrapper>();
		for (final Package pkg : this.repository.getPackages()) {
			packages.add(new PackageWrapper(pkg));
			packages.addAll(this.getPackages(pkg));
		}
		return packages;
	}

	private Collection<PackageWrapper> getPackages(Package pkg) {
		final Collection<PackageWrapper> packages = new LinkedList<PackageWrapper>();
		for (final Package subpkg : pkg.getPackages()) {
			packages.add(new PackageWrapper(subpkg));
			packages.addAll(this.getPackages(subpkg));
		}
		return packages;
	}

	public Collection<ComponentWrapper> getComponents() {
		final Collection<ComponentWrapper> components = new LinkedList<ComponentWrapper>();
		for (final Package pkg : this.repository.getPackages()) {
			components.addAll(this.getComponents(pkg));
		}
		return components;
	}

	private Collection<ComponentWrapper> getComponents(Package pkg) {
		final Collection<ComponentWrapper> components = new LinkedList<ComponentWrapper>();
		for (final Package subpkg : pkg.getPackages()) {
			components.addAll(this.getComponents(subpkg));
		}
		for (final Component component : pkg.getComponents()) {
			components.add(new ComponentWrapper(component));
		}
		return components;
	}
}
