package de.proskor.model.impl;

import cli.EA.ICollection;
import cli.EA.IConnector;
import cli.EA.IElement;
import cli.EA.IRepository;
import de.proskor.model.Collection;
import de.proskor.model.Connector;
import de.proskor.model.Element;
import de.proskor.model.Package;

class ElementImpl implements Element {
	private final IRepository repository;
	private final int id;

	public ElementImpl(IRepository repository, int id) {
		this.repository = repository;
		this.id = id;
	}

	private IElement getPeer() {
		return (IElement) this.repository.GetElementByID(this.id);
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getGuid() {
		final IElement peer = this.getPeer();
		return (String) peer.get_ElementGUID();
	}

	@Override
	public String getName() {
		final IElement peer = this.getPeer();
		return (String) peer.get_Name();
	}

	@Override
	public void setName(String name) {
		final IElement peer = this.getPeer();
		peer.set_Name(name);
	}

	@Override
	public String getDescription() {
		final IElement peer = this.getPeer();
		return (String) peer.get_Notes();
	}

	@Override
	public void setDescription(String description) {
		final IElement peer = this.getPeer();
		peer.set_Notes(description);
	}

	@Override
	public String getStereotype() {
		final IElement peer = this.getPeer();
		return (String) peer.get_StereotypeEx();
	}

	@Override
	public void setStereotype(String stereotype) {
		final IElement peer = this.getPeer();
		peer.set_StereotypeEx(stereotype);
	}

	@Override
	public boolean isChild() {
		final IElement peer = this.getPeer();
		return peer.get_ParentID() != 0;
	}

	@Override
	public Package getPackage() {
		final IElement peer = this.getPeer();
		final int packageId = peer.get_PackageID();
		return new PackageImpl(this.repository, packageId);
	}

	@Override
	public Element getParent() {
		final IElement peer = this.getPeer();
		final int parentId = peer.get_ParentID();

		if (parentId == 0)
			throw new IllegalStateException();

		return new ElementImpl(this.repository, parentId);
	}

	@Override
	public Collection<Element> getElements() {
		final IElement peer = this.getPeer();
		final ICollection elements = (ICollection) peer.get_Elements();
		return new CollectionImpl<Element, IElement>(elements) {
			@Override
			protected boolean matches(IElement object, Element element) {
				return object.get_ElementID() == element.getId();
			}

			@Override
			protected Element create(IElement element) {
				return new ElementImpl(ElementImpl.this.repository, element.get_ElementID());
			}
		};
	}

	@Override
	public Collection<Connector> getConnectors() {
		final IElement peer = this.getPeer();
		final ICollection collection = (ICollection) peer.get_Connectors();
		return new CollectionImpl<Connector, IConnector>(collection) {
			@Override
			protected boolean matches(IConnector object, Connector element) {
				return object.get_ConnectorID() == element.getId();
			}

			@Override
			protected Connector create(IConnector element) {
				return new ConnectorImpl(ElementImpl.this.repository, element.get_ConnectorID());
			}

			@Override
			public Connector add(String name, String type) {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public Connector connectTo(Element target) {
		final IElement peer = this.getPeer();
		final ICollection collection = (ICollection) peer.get_Connectors();
		final IConnector connector = (IConnector) collection.AddNew("", "Connector");
		connector.set_ClientID(this.id);
		connector.set_SupplierID(target.getId());
		connector.Update();
		return new ConnectorImpl(this.repository, connector.get_ConnectorID());
	}

	@Override
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (that instanceof ElementImpl)
			return this.id == ((ElementImpl) that).id;

		return false;
	}

	@Override
	public int hashCode() {
		return this.id;
	}
}
