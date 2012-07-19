package de.proskor.model.impl;

import cli.EA.ICollection;
import cli.EA.IDiagram;
import cli.EA.IDiagramObject;
import cli.EA.IRepository;
import de.proskor.model.Diagram;
import de.proskor.model.Element;
import de.proskor.model.Node;

class NodeImpl implements Node {
	private final IRepository repository;
	private final int diagramId;
	private final int nodeId;

	public NodeImpl(IRepository repository, int diagramId, int nodeId) {
		this.repository = repository;
		this.diagramId = diagramId;
		this.nodeId = nodeId;
	}

	private IDiagramObject getPeer() {
		final IDiagram diagram = (IDiagram) this.repository.GetDiagramByID(this.diagramId);
		final ICollection collection = (ICollection) diagram.get_DiagramObjects();
		final short count = collection.get_Count();
		for (short i = 0; i < count; i++) {
			final IDiagramObject node = (IDiagramObject) collection.GetAt(i);
			if (node.get_InstanceID() == this.nodeId)
				return node;
		}
		throw new IllegalStateException();
	}

	@Override
	public int getId() {
		return this.nodeId;
	}

	@Override
	public Element getElement() {
		final IDiagramObject peer = this.getPeer();
		final int elementId = peer.get_ElementID();
		return new ElementImpl(this.repository, elementId);
	}

	@Override
	public Diagram getDiagram() {
		return new DiagramImpl(this.repository, this.diagramId);
	}

	@Override
	public int getLeft() {
		final IDiagramObject peer = this.getPeer();
		return peer.get_left();
	}

	@Override
	public void setLeft(int left) {
		final IDiagramObject peer = this.getPeer();
		peer.set_left(peer.get_right() - peer.get_left() + left);
	}

	@Override
	public int getTop() {
		final IDiagramObject peer = this.getPeer();
		return -peer.get_top();
	}

	@Override
	public void setTop(int top) {
		final IDiagramObject peer = this.getPeer();
		peer.set_top(peer.get_bottom() - peer.get_top() - top);
	}

	@Override
	public int getRight() {
		final IDiagramObject peer = this.getPeer();
		return peer.get_right();
	}

	@Override
	public void setRight(int right) {
		final IDiagramObject peer = this.getPeer();
		peer.set_right(right);
	}

	@Override
	public int getBottom() {
		final IDiagramObject peer = this.getPeer();
		return -peer.get_bottom();
	}

	@Override
	public void setBottom(int bottom) {
		final IDiagramObject peer = this.getPeer();
		peer.set_bottom(-bottom);
	}

	@Override
	public int getSequence() {
		final IDiagramObject peer = this.getPeer();
		return Integer.valueOf(peer.get_Sequence().toString());
	}

	@Override
	public void setSequence(int sequence) {
		final IDiagramObject peer = this.getPeer();
		peer.set_Sequence(sequence);
	}
}
