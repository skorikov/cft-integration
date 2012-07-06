package de.proskor.model.impl;

import cli.EA.IConnector;
import de.proskor.model.Connector;
import de.proskor.model.Element;

public class ConnectorImpl implements Connector {
	private final IConnector peer;
	private final RepositoryImpl repository;

	public ConnectorImpl(IConnector peer, RepositoryImpl repository) {
		this.peer = peer;
		this.repository = repository;
	}
	
	@Override
	public int getId() {
		return this.peer.get_ConnectorID();
	}

	@Override
	public String getGuid() {
		return (String) this.peer.get_ConnectorGUID();
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
	public Element getSource() {
		return this.repository.getElementById(this.peer.get_ClientID());
	}

	@Override
	public void setSource(Element source) {
		this.peer.set_ClientID(source.getId());
		// TODO: Update
	}

	@Override
	public Element getTarget() {
		return this.repository.getElementById(this.peer.get_SupplierID());
	}

	@Override
	public void setTarget(Element target) {
		this.peer.set_SupplierID(target.getId());
		// TODO: Update
	}
}
