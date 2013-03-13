package de.proskor.cft.merge.ui;

import de.proskor.cft.Package;

final class PackageWrapper extends ElementWrapper {
	private final Package pkg;

	public PackageWrapper(Package pkg) {
		super(pkg);
		this.pkg = pkg;
	}

	public Package getPackage() {
		return this.pkg;
	}
}
