package de.proskor.model.impl;

import cli.EA.ICollection;
import cli.EA.IPackage;
import de.proskor.model.Collection;
import de.proskor.model.Model;
import de.proskor.model.Package;

public class ModelImpl implements Model {
	private final IPackage peer;
	private Collection<Package> packages = null;

	public ModelImpl(IPackage peer) {
		this.peer = peer;
	}
	
	@Override
	public int getId() {
		return this.peer.get_PackageID();
	}

	@Override
	public String getGuid() {
		return (String) this.peer.get_PackageGUID();
	}

	@Override
	public String getName() {
		return (String) this.peer.get_Name();
	}

	@Override
	public void setName(String name) {
		this.peer.set_Name(name);
	}

	@Override
	public String getDescription() {
		return (String) this.peer.get_Notes();
	}

	@Override
	public void setDescription(String description) {
		this.peer.set_Notes(description);
	}

	@Override
	public Collection<Package> getPackages() {
		if (this.packages == null) {
			final ICollection packages = (ICollection) this.peer.get_Packages();
			this.packages = new CollectionImpl<Package, IPackage>(packages) {
				@Override
				protected int getId(IPackage element) {
					return element.get_PackageID();
				}
	
				@Override
				protected Package create(ICollection collection, IPackage element) {
					element.Update();
					collection.Refresh();
					return new PackageImpl(element, ModelImpl.this);
				}
			};
		}
		
		return this.packages;
	}
}
