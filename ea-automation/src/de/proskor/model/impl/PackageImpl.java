package de.proskor.model.impl;

import cli.EA.ICollection;
import cli.EA.IDiagram;
import cli.EA.IElement;
import cli.EA.IPackage;
import cli.EA.IRepository;
import de.proskor.model.Collection;
import de.proskor.model.Diagram;
import de.proskor.model.Element;
import de.proskor.model.Package;

class PackageImpl extends IdentityImpl<IPackage> implements Package {
	public PackageImpl(IRepository repository, int id) {
		super(repository, id);
	}

	@Override
	protected IPackage getPeer() {
		return (IPackage) this.getRepository().GetPackageByID(this.getId());
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
		peer.Update();
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
		peer.Update();
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
		peer.Update();
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
		return new ElementImpl(this.getRepository(), elementId);
	}

	@Override
	public Package getParent() {
		final IPackage peer = this.getPeer();
		final int parentId = peer.get_ParentID();

		if (parentId == 0)
			throw new IllegalStateException();

		return new PackageImpl(this.getRepository(), parentId);
	}

	@Override
	public Collection<Package> getPackages() {
		final IPackage peer = this.getPeer();
		final ICollection collection = (ICollection) peer.get_Packages();
		return new CollectionImpl<Package, IPackage>(collection) {
			@Override
			protected boolean matches(IPackage object, Package element) {
				return object.get_PackageID() == element.getId();
			}

			@Override
			protected Package create(IPackage element) {
				element.Update();
				return new PackageImpl(PackageImpl.this.getRepository(), element.get_PackageID());
			}

			@Override
			public void clear() {
				super.clear();
				PackageImpl.this.getRepository().RefreshModelView(PackageImpl.this.getId());
			}

			@Override
			public void remove(int index) {
				super.remove(index);
				PackageImpl.this.getRepository().RefreshModelView(PackageImpl.this.getId());
			}
		};
	}

	@Override
	public Collection<Element> getElements() {
		final IPackage peer = this.getPeer();
		final ICollection collection = (ICollection) peer.get_Elements();
		return new CollectionImpl<Element, IElement>(collection) {
			@Override
			protected boolean matches(IElement object, Element element) {
				return object.get_ElementID() == element.getId();
			}

			@Override
			protected Element create(IElement element) {
				return new ElementImpl(PackageImpl.this.getRepository(), element.get_ElementID());
			}
		};
	}

	@Override
	public Collection<Diagram> getDiagrams() {
		final IPackage peer = this.getPeer();
		final ICollection collection = (ICollection) peer.get_Diagrams();
		return new CollectionImpl<Diagram, IDiagram>(collection) {
			@Override
			protected boolean matches(IDiagram object, Diagram element) {
				return object.get_DiagramID() == element.getId();
			}

			@Override
			protected Diagram create(IDiagram element) {
				return new DiagramImpl(PackageImpl.this.getRepository(), element.get_DiagramID());
			}
		};
	}

	@Override
	public String toString() {
		return "PACKAGE[" + this.getId() + "|" + this.getName() + "]";
	}

	@Override
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (that instanceof PackageImpl)
			return this.getId() == ((PackageImpl) that).getId();

		return false;
	}

	@Override
	public int hashCode() {
		return this.getId();
	}
}
