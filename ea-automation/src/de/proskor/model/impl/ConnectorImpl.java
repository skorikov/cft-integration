package de.proskor.model.impl;

import cli.EA.IConnector;
import cli.EA.IRepository;
import de.proskor.model.Connector;
import de.proskor.model.Element;

class ConnectorImpl implements Connector {
	private final IRepository repository;
	private final int id;

	public ConnectorImpl(IRepository repository, int id) {
		this.repository = repository;
		this.id = id;
	}

	private IConnector getPeer() {
		return (IConnector) repository.GetConnectorByID(this.id);
	}

	@Override
	public int getId() {
		return this.id;
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
	}

	@Override
	public Element getSource() {
		final IConnector peer = this.getPeer();
		final int sourceId = peer.get_ClientID();
		return new ElementImpl(this.repository, sourceId);
	}

	@Override
	public void setSource(Element source) {
		final IConnector peer = this.getPeer();
		peer.set_ClientID(source.getId());
		// TODO: Update
	}

	@Override
	public Element getTarget() {
		final IConnector peer = this.getPeer();
		final int targetId = peer.get_SupplierID();
		return new ElementImpl(this.repository, targetId);
	}

	@Override
	public void setTarget(Element target) {
		final IConnector peer = this.getPeer();
		peer.set_SupplierID(target.getId());
		// TODO: Update
	}
}
