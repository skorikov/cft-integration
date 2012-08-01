package de.proskor.model.impl;

import cli.EA.ICollection;
import cli.EA.IConnector;
import cli.EA.IElement;
import cli.EA.IRepository;
import cli.EA.ITaggedValue;
import de.proskor.model.Collection;
import de.proskor.model.Connector;
import de.proskor.model.Element;
import de.proskor.model.Package;
import de.proskor.model.TaggedValue;

class ElementImpl extends IdentityImpl<IElement> implements Element {
	public ElementImpl(IRepository repository, int id) {
		super(repository, id);
	}

	@Override
	protected IElement getPeer() {
		return (IElement) this.getRepository().GetElementByID(this.getId());
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
		peer.Update();
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
		peer.Update();
	}

	@Override
	public String getType() {
		final IElement peer = this.getPeer();
		return (String) peer.get_Type();
	}

	@Override
	public void setType(String type) {
		final IElement peer = this.getPeer();
		peer.set_Type(type);
		peer.Update();
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
		peer.Update();
	}

	@Override
	public String getAuthor() {
		final IElement peer = this.getPeer();
		return (String) peer.get_Author();
	}

	@Override
	public void setAuthor(String author) {
		final IElement peer = this.getPeer();
		peer.set_Author(author);
		peer.Update();
	}

	@Override
	public boolean isChild() {
		final IElement peer = this.getPeer();
		return peer.get_ParentID() != 0;
	}

	@Override
	public Element getParent() {
		final IElement peer = this.getPeer();
		final int parentId = peer.get_ParentID();

		if (parentId == 0)
			throw new IllegalStateException();

		return new ElementImpl(this.getRepository(), parentId);
	}

	@Override
	public void setParent(Element parent) {
		final IElement peer = this.getPeer();
		peer.set_ParentID(parent.getId());
		peer.Update();
	}

	@Override
	public Package getPackage() {
		final IElement peer = this.getPeer();
		final int packageId = peer.get_PackageID();
		return new PackageImpl(this.getRepository(), packageId);
	}

	@Override
	public void setPackage(Package pkg) {
		final IElement peer = this.getPeer();
		peer.set_PackageID(pkg.getId());
		peer.Update();
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
				return new ElementImpl(ElementImpl.this.getRepository(), element.get_ElementID());
			}
		};
	}

	@Override
	public Collection<TaggedValue> getTaggedValues() {
		final IElement peer = this.getPeer();
		final ICollection taggedValues = (ICollection) peer.get_TaggedValues();
		return new CollectionImpl<TaggedValue, ITaggedValue>(taggedValues) {
			@Override
			protected boolean matches(ITaggedValue object, TaggedValue element) {
				return object.get_PropertyID() == element.getId();
			}

			@Override
			protected TaggedValue create(ITaggedValue element) {
				return new TaggedValueImpl(ElementImpl.this.getRepository(), ElementImpl.this.getId(), element.get_PropertyID());
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
				return new ConnectorImpl(ElementImpl.this.getRepository(), element.get_ConnectorID());
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
		final IConnector connector = (IConnector) collection.AddNew("", Connector.CONNECTOR);
		connector.set_ClientID(this.getId());
		connector.set_SupplierID(target.getId());
		connector.Update();
		return new ConnectorImpl(this.getRepository(), connector.get_ConnectorID());
	}

	@Override
	public String toString() {
		return "ELEMENT[" + this.getId() + "|" + this.getName() + "]";
	}

	@Override
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (that instanceof ElementImpl)
			return this.getId() == ((ElementImpl) that).getId();

		return false;
	}

	@Override
	public int hashCode() {
		return this.getId();
	}
}
