package de.proskor.model.impl;

import cli.EA.ICollection;
import cli.EA.IDiagram;
import cli.EA.IDiagramObject;
import de.proskor.model.Collection;
import de.proskor.model.Diagram;
import de.proskor.model.Node;
import de.proskor.model.Package;

public class DiagramImpl implements Diagram {
	private final IDiagram peer;
	private final Package pkg;
	private Collection<Node> nodes = null;

	public DiagramImpl(IDiagram peer, Package pkg) {
		this.peer = peer;
		this.pkg = pkg;
	}
	
	@Override
	public int getId() {
		return this.peer.get_DiagramID();
	}

	@Override
	public String getGuid() {
		return (String) this.peer.get_DiagramGUID();
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
	public Package getPackage() {
		return this.pkg;
	}

	@Override
	public Collection<Node> getNodes() {
		if (this.nodes == null) {
			final ICollection nodes = (ICollection) this.peer.get_DiagramObjects();
			this.nodes = new CollectionImpl<Node, IDiagramObject>(nodes) {
				@Override
				protected boolean matches(IDiagramObject object, Node element) {
					return object.get_InstanceID() == element.getId();
				}
	
				@Override
				protected Node create(ICollection collection, IDiagramObject element) {
					// TODO
					return null;
				/*	element.Update();
					collection.Refresh();
					return new ElementImpl(element, ElementImpl.this, ElementImpl.this.pkg);*/
				}
			};
		}
		
		return this.nodes;
	}
}
