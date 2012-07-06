package de.proskor.model.impl;

import cli.EA.IDiagramObject;
import de.proskor.model.Diagram;
import de.proskor.model.Element;
import de.proskor.model.Node;

public class NodeImpl implements Node {
	private final IDiagramObject peer;
	private final Diagram diagram;
	private final RepositoryImpl repository;

	public NodeImpl(IDiagramObject peer, Diagram diagram, RepositoryImpl repository) {
		this.peer = peer;
		this.diagram = diagram;
		this.repository = repository;
	}
	
	@Override
	public int getId() {
		return this.peer.get_InstanceID();
	}

	@Override
	public Element getElement() {
		return this.repository.getElementById(this.peer.get_ElementID());
	}

	@Override
	public Diagram getDiagram() {
		return this.diagram;
	}

	@Override
	public int getLeft() {
		return this.peer.get_left();
	}

	@Override
	public void setLeft(int left) {
		this.peer.set_left(this.peer.get_right() - this.peer.get_left() + left);
	}

	@Override
	public int getTop() {
		return -this.peer.get_top();
	}

	@Override
	public void setTop(int top) {
		this.peer.set_top(this.peer.get_bottom() - this.peer.get_top() - top);
	}

	@Override
	public int getRight() {
		return this.peer.get_right();
	}

	@Override
	public void setRight(int right) {
		this.peer.set_right(right);
	}

	@Override
	public int getBottom() {
		return -this.peer.get_bottom();
	}

	@Override
	public void setBottom(int bottom) {
		this.peer.set_bottom(-bottom);
	}

	@Override
	public int getSequence() {
		return Integer.valueOf(this.peer.get_Sequence().toString());
	}

	@Override
	public void setSequence(int sequence) {
		this.peer.set_Sequence(sequence);
	}
}
