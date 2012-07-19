package de.proskor.model.impl;

import cli.EA.ICollection;
import cli.EA.IDiagram;
import cli.EA.IDiagramObject;
import cli.EA.IRepository;
import de.proskor.model.Collection;
import de.proskor.model.Diagram;
import de.proskor.model.Node;
import de.proskor.model.Package;

class DiagramImpl implements Diagram {
	private final IRepository repository;
	private final int id;

	public DiagramImpl(IRepository repository, int id) {
		this.repository = repository;
		this.id = id;
	}

	private IDiagram getPeer() {
		return (IDiagram) this.repository.GetDiagramByID(this.id);
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getGuid() {
		final IDiagram peer = this.getPeer();
		return (String) peer.get_DiagramGUID();
	}

	@Override
	public String getName() {
		final IDiagram peer = this.getPeer();
		return (String) peer.get_Name();
	}

	@Override
	public void setName(String name) {
		final IDiagram peer = this.getPeer();
		peer.set_Name(name);
	}

	@Override
	public String getDescription() {
		final IDiagram peer = this.getPeer();
		return (String) peer.get_Notes();
	}

	@Override
	public void setDescription(String description) {
		final IDiagram peer = this.getPeer();
		peer.set_Notes(description);
	}

	@Override
	public String getStereotype() {
		final IDiagram peer = this.getPeer();
		return (String) peer.get_Stereotype();
	}

	@Override
	public void setStereotype(String stereotype) {
		final IDiagram peer = this.getPeer();
		peer.set_Stereotype(stereotype);
	}

	@Override
	public Package getPackage() {
		final IDiagram peer = this.getPeer();
		final int packageId = peer.get_PackageID();
		return new PackageImpl(this.repository, packageId);
	}

	@Override
	public Collection<Node> getNodes() {
		final IDiagram peer = this.getPeer();
		final ICollection collection = (ICollection) peer.get_DiagramObjects();
		return new CollectionImpl<Node, IDiagramObject>(collection) {
			@Override
			protected boolean matches(IDiagramObject object, Node element) {
				return object.get_InstanceID() == element.getId();
			}

			@Override
			protected Node create(IDiagramObject element) {
				return new NodeImpl(DiagramImpl.this.repository, DiagramImpl.this.id, element.get_InstanceID());
			}
		};
	}
}
