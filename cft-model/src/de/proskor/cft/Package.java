package de.proskor.cft;

import java.util.Collection;

public interface Package extends Container {
	@Override
	Package getParent();

	Collection<Package> getPackages();

	Collection<Component> getComponents();
}
