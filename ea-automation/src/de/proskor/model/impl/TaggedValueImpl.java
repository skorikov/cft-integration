package de.proskor.model.impl;

import cli.EA.ICollection;
import cli.EA.IElement;
import cli.EA.IRepository;
import cli.EA.ITaggedValue;
import de.proskor.model.Element;
import de.proskor.model.TaggedValue;

class TaggedValueImpl extends IdentityImpl<ITaggedValue> implements TaggedValue {
	private final int elementId;

	public TaggedValueImpl(IRepository repository, int elementId, int valueId) {
		super(repository, valueId);
		this.elementId = elementId;
	}

	@Override
	protected ITaggedValue getPeer() {
		final IElement element = (IElement) this.getRepository().GetElementByID(this.elementId);
		final ICollection collection = (ICollection) element.get_TaggedValues();
		final short count = collection.get_Count();
		for (short i = 0; i < count; i++) {
			final ITaggedValue tv = (ITaggedValue) collection.GetAt(i);
			if (tv.get_PropertyID() == this.getId())
				return tv;
		}
		throw new IllegalStateException();
	}

	@Override
	public String getGuid() {
		final ITaggedValue peer = this.getPeer();
		return (String) peer.get_PropertyGUID();
	}

	@Override
	public String getName() {
		final ITaggedValue peer = this.getPeer();
		return (String) peer.get_Name();
	}

	@Override
	public void setName(String name) {
		final ITaggedValue peer = this.getPeer();
		peer.set_Name(name);
		peer.Update();
	}

	@Override
	public String getDescription() {
		final ITaggedValue peer = this.getPeer();
		return (String) peer.get_Notes();
	}

	@Override
	public void setDescription(String description) {
		final ITaggedValue peer = this.getPeer();
		peer.set_Notes(description);
		peer.Update();
	}

	@Override
	public String getValue() {
		final ITaggedValue peer = this.getPeer();
		return (String) peer.get_Value();
	}

	@Override
	public void setValue(String value) {
		final ITaggedValue peer = this.getPeer();
		peer.set_Value(value);
		peer.Update();
	}

	@Override
	public Element getElement() {
		final ITaggedValue peer = this.getPeer();
		final int elementId = peer.get_ElementID();
		return new ElementImpl(this.getRepository(), elementId);
	}

	@Override
	public String toString() {
		return "TAGGEDVALUE[" + this.getId() + "|" + this.getName() + "]";
	}

	@Override
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (that instanceof TaggedValueImpl)
			return this.getId() == ((TaggedValueImpl) that).getId();

		return false;
	}

	@Override
	public int hashCode() {
		return this.getId();
	}
}
