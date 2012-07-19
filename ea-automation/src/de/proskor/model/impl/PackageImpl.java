package de.proskor.model.impl;

import cli.EA.ICollection;
import cli.EA.IElement;
import cli.EA.IPackage;
import cli.EA.IRepository;
import de.proskor.model.Collection;
import de.proskor.model.Diagram;
import de.proskor.model.Element;
import de.proskor.model.Package;

public class PackageImpl implements Package {
	private final IRepository repository;
	private final int id;

	public PackageImpl(IRepository repository, int id) {
		this.repository = repository;
		this.id = id;
	}

	private IPackage getPeer() {
		return (IPackage) this.repository.GetPackageByID(this.id);
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getGuid() {
		final IPackage peer = this.getPeer();
		return (String) peer.get_PackageGUID();
	}

	@Override
	public String getName() {
		final IPackage peer = this.getPeer();
		return (String) peer.get_Name();
	}

	@Override
	public void setName(String name) {
		final IPackage peer = this.getPeer();
		peer.set_Name(name);
	}

	@Override
	public String getDescription() {
		final IPackage peer = this.getPeer();
		return (String) peer.get_Notes();
	}

	@Override
	public void setDescription(String description) {
		final IPackage peer = this.getPeer();
		peer.set_Notes(description);
	}

	@Override
	public String getStereotype() {
		final IPackage peer = this.getPeer();
		return (String) peer.get_StereotypeEx();
	}

	@Override
	public void setStereotype(String stereotype) {
		final IPackage peer = this.getPeer();
		peer.set_StereotypeEx(stereotype);
	}

	@Override
	public boolean isModel() {
		final IPackage peer = this.getPeer();
		return peer.get_ParentID() == 0;
	}

	@Override
	public Element getElement() {
		final IPackage peer = this.getPeer();
		final IElement element = (IElement) peer.get_Element();
		final int elementId = element.get_ElementID();
		return new ElementImpl(this.repository, elementId);
	}

	@Override
	public Package getParent() {
		final IPackage peer = this.getPeer();
		final int parentId = peer.get_ParentID();

		if (parentId == 0)
			throw new IllegalStateException();

		return new PackageImpl(this.repository, parentId);
	}

	@Override
	public Collection<Package> getPackages() {
		final IPackage peer = this.getPeer();
		final ICollection packages = (ICollection) peer.get_Packages();
		return new CollectionImpl<Package, IPackage>(packages) {
			@Override
			protected boolean matches(IPackage object, Package element) {
				return object.get_PackageID() == element.getId();
			}

			@Override
			protected Package create(IPackage element) {
				return new PackageImpl(PackageImpl.this.repository, element.get_PackageID());
			}
		};
	}

	@Override
	public Collection<Element> getElements() {
		final IPackage peer = this.getPeer();
		final ICollection elements = (ICollection) peer.get_Elements();
		return new CollectionImpl<Element, IElement>(elements) {
			@Override
			protected boolean matches(IElement object, Element element) {
				return object.get_ElementID() == element.getId();
			}

			@Override
			protected Element create(IElement element) {
				return new ElementImpl(PackageImpl.this.repository, element.get_ElementID());
			}
		};
	}

	@Override
	public Collection<Diagram> getDiagrams() {
		// TODO
		return null;
	}
}
