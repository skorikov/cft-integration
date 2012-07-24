package de.proskor.model.impl;

import cli.EA.IConnector;
import cli.EA.IRepository;
import de.proskor.model.Connector;
import de.proskor.model.Element;

class ConnectorImpl extends IdentityImpl<IConnector> implements Connector {
	public ConnectorImpl(IRepository repository, int id) {
		super(repository, id);
	}

	@Override
	protected IConnector getPeer() {
		return (IConnector) getRepository().GetConnectorByID(this.getId());
	}

	@Override
	public String getGuid() {
		final IConnector peer = this.getPeer();
		return (String) peer.get_ConnectorGUID();
	}

	@Override
	public String getName() {
		final IConnector peer = this.getPeer();
		return (String) peer.get_Name();
	}

	@Override
	public void setName(String name) {
		final IConnector peer = this.getPeer();
		peer.set_Name(name);
		peer.Update();
	}

	@Override
	public String getDescription() {
		final IConnector peer = this.getPeer();
		return (String) peer.get_Notes();
	}

	@Override
	public void setDescription(String description) {
		final IConnector peer = this.getPeer();
		peer.set_Notes(description);
		peer.Update();
	}

	@Override
	public String getType() {
		final IConnector peer = this.getPeer();
		return (String) peer.get_Type();
	}

	@Override
	public void setType(String type) {
		final IConnector peer = this.getPeer();
		peer.set_Type(type);
		peer.Update();
	}

	@Override
	public String getStereotype() {
		final IConnector peer = this.getPeer();
		return (String) peer.get_Stereotype();
	}

	@Override
	public void setStereotype(String stereotype) {
		final IConnector peer = this.getPeer();
		peer.set_Stereotype(stereotype);
		peer.Update();
	}

	@Override
	public Element getSource() {
		final IConnector peer = this.getPeer();
		final int sourceId = peer.get_ClientID();
		return new ElementImpl(this.getRepository(), sourceId);
	}

	@Override
	public void setSource(Element source) {
		final IConnector peer = this.getPeer();
		peer.set_ClientID(source.getId());
		peer.Update();
	}

	@Override
	public Element getTarget() {
		final IConnector peer = this.getPeer();
		final int targetId = peer.get_SupplierID();
		return new ElementImpl(this.getRepository(), targetId);
	}

	@Override
	public void setTarget(Element target) {
		final IConnector peer = this.getPeer();
		peer.set_SupplierID(target.getId());
		peer.Update();
	}

	@Override
	public String toString() {
		return "CONNECTOR[" + this.getId() + "|" + this.getName() + "]";
	}

	@Override
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (that instanceof ConnectorImpl)
			return this.getId() == ((ConnectorImpl) that).getId();

		return false;
	}

	@Override
	public int hashCode() {
		return this.getId();
	}
}
