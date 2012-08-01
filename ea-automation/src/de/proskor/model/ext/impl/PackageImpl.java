package de.proskor.model.ext.impl;

import de.proskor.model.ext.Package;

public class PackageImpl implements Package {
	private final de.proskor.model.Package peer;

	public PackageImpl(de.proskor.model.Package peer) {
		this.peer = peer;
	}

	de.proskor.model.Package getPeer() {
		return this.peer;
	}
}
