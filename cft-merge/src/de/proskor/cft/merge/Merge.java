package de.proskor.cft.merge;

import de.proskor.cft.Component;
import de.proskor.cft.Package;

public final class Merge {
	public void run(Component left, Component right, Package target) {
		final Component result = target.createComponent();
		result.setName(left.getName() + "-merged-with-" + right.getName());
	}
}
