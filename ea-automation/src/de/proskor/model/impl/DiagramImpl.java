package de.proskor.model.impl;

import cli.EA.ICollection;
import cli.EA.IDiagram;
import cli.EA.IDiagramObject;
import cli.EA.IRepository;
import de.proskor.model.Collection;
import de.proskor.model.Diagram;
import de.proskor.model.Element;
import de.proskor.model.Node;
import de.proskor.model.Package;

class DiagramImpl extends IdentityImpl<IDiagram> implements Diagram {
	public DiagramImpl(IRepository repository, int id) {
		super(repository, id);
	}

	@Override
	protected IDiagram getPeer() {
		return (IDiagram) this.getRepository().GetDiagramByID(this.getId());
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
		peer.Update();
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
		peer.Update();
	}

	@Override
	public String getType() {
		final IDiagram peer = this.getPeer();
		return (String) peer.get_Type();
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
		peer.Update();
	}

	@Override
	public Package getPackage() {
		final IDiagram peer = this.getPeer();
		final int packageId = peer.get_PackageID();
		return new PackageImpl(this.getRepository(), packageId);
	}

	@Override
	public void setPackage(Package pkg) {
		final IDiagram peer = this.getPeer();
		peer.set_PackageID(pkg.getId());
		peer.Update();
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
				return new NodeImpl(DiagramImpl.this.getRepository(), DiagramImpl.this.getId(), element.get_InstanceID());
			}

			@Override
			public Node add(String name, String type) {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public Node createNode(Element element) {
		final IDiagram peer = this.getPeer();
		final ICollection collection = (ICollection) peer.get_DiagramObjects();
		final IDiagramObject node = (IDiagramObject) collection.AddNew(element.getName(), Node.NODE);
		node.set_ElementID(element.getId());
		node.Update();
		return new NodeImpl(this.getRepository(), peer.get_DiagramID(), node.get_InstanceID());
	}

	@Override
	public String toString() {
		return "DIAGRAM[" + this.getId() + "|" + this.getName() + "]";
	}

	@Override
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (that instanceof DiagramImpl)
			return this.getId() == ((DiagramImpl) that).getId();

		return false;
	}

	@Override
	public int hashCode() {
		return this.getId();
	}
}
