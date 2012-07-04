package de.proskor.model.impl;

import cli.EA.ICollection;
import cli.EA.IDiagram;
import cli.EA.IPackage;
import de.proskor.model.Collection;
import de.proskor.model.Diagram;
import de.proskor.model.Element;
import de.proskor.model.Package;

public class PackageImpl implements Package {
	private final IPackage peer;
	private final Package parent;
	private final RepositoryImpl repository;
	private Collection<Package> packages = null;
	private Collection<Diagram> diagrams = null;

	public PackageImpl(IPackage peer, Package parent, RepositoryImpl repository) {
		this.peer = peer;
		this.parent = parent;
		this.repository = repository;
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
	public Element getElement() {
		return null;
	}

	@Override
	public Package getParent() {
		return this.parent;
	}

	@Override
	public Collection<Package> getPackages() {
		if (this.packages == null) {
			final ICollection packages = (ICollection) this.peer.get_Packages();
			this.packages = new CollectionImpl<Package, IPackage>(packages) {
				@Override
				protected boolean matches(IPackage object, Package element) {
					return object.get_PackageID() == element.getId();
				}
	
				@Override
				protected Package create(ICollection collection, IPackage element) {
					element.Update();
					collection.Refresh();
					return new PackageImpl(element, PackageImpl.this, PackageImpl.this.repository);
				}
			};
		}
		
		return this.packages;
	}

	@Override
	public Collection<Diagram> getDiagrams() {
		if (this.diagrams == null) {
			final ICollection diagrams = (ICollection) this.peer.get_Diagrams();
			this.diagrams = new CollectionImpl<Diagram, IDiagram>(diagrams) {
				@Override
				protected boolean matches(IDiagram object, Diagram element) {
					return object.get_DiagramID() == element.getId();
				}
	
				@Override
				protected Diagram create(ICollection collection, IDiagram element) {
					element.Update();
					collection.Refresh();
					return new DiagramImpl(element, PackageImpl.this, PackageImpl.this.repository);
				}
			};
		}
		
		return this.diagrams;
	}
}
