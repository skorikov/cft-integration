package de.proskor.model.impl;

import cli.EA.ICollection;
import cli.EA.IPackage;
import de.proskor.model.Collection;
import de.proskor.model.Container;
import de.proskor.model.Package;

public class PackageImpl implements Package {
	private final IPackage peer;
	private final Container parent;
	private Collection<Package> packages = null;

	public PackageImpl(IPackage peer, Container parent) {
		this.peer = peer;
		this.parent = parent;
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
	public String getStereotype() {
		return (String) this.peer.get_StereotypeEx();
	}

	@Override
	public void setStereotype(String stereotype) {
		this.peer.set_StereotypeEx(stereotype);
	}

	@Override
	public Container getParent() {
		return this.parent;
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
					return new PackageImpl(element, PackageImpl.this);
				}
			};
		}
		
		return this.packages;
	}
}
