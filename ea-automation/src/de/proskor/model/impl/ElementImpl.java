package de.proskor.model.impl;

import cli.EA.ICollection;
import cli.EA.IElement;
import de.proskor.model.Collection;
import de.proskor.model.Element;
import de.proskor.model.Package;

public class ElementImpl implements Element {
	private final IElement peer;
	private final Element parent;
	private final Package pkg;
	private Collection<Element> elements = null;

	public ElementImpl(IElement peer, Element parent, Package pkg) {
		this.peer = peer;
		this.parent = parent;
		this.pkg = pkg;
	}
	
	@Override
	public int getId() {
		return this.peer.get_ElementID();
	}

	@Override
	public String getGuid() {
		return (String) this.peer.get_ElementGUID();
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
		return (String) this.peer.get_Stereotype();
	}

	@Override
	public void setStereotype(String stereotype) {
		this.peer.set_Stereotype(stereotype);
	}

	@Override
	public Element getParent() {
		return this.parent;
	}

	@Override
	public Package getPackage() {
		return this.pkg;
	}

	@Override
	public Collection<Element> getElements() {
		if (this.elements == null) {
			final ICollection elements = (ICollection) this.peer.get_Elements();
			this.elements = new CollectionImpl<Element, IElement>(elements) {
				@Override
				protected boolean matches(IElement object, Element element) {
					return object.get_ElementID() == element.getId();
				}
	
				@Override
				protected Element create(ICollection collection, IElement element) {
					element.Update();
					collection.Refresh();
					return new ElementImpl(element, ElementImpl.this, ElementImpl.this.pkg);
				}
			};
		}
		
		return this.elements;
	}
}
