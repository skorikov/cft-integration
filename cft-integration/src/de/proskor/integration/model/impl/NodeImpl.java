package de.proskor.integration.model.impl;

import de.proskor.integration.model.Node;

abstract class NodeImpl implements Node {
	private int left;
	private int top;
	private int right;
	private int bottom;

	@Override
	public int getLeft() {
		return this.left;
	}

	@Override
	public void setLeft(int left) {
		this.left = left;
	}

	@Override
	public int getTop() {
		return this.top;
	}

	@Override
	public void setTop(int top) {
		this.top = top;
	}

	@Override
	public int getRight() {
		return this.right;
	}

	@Override
	public void setRight(int right) {
		this.right = right;
	}

	@Override
	public int getBottom() {
		return this.bottom;
	}

	@Override
	public void setBottom(int bottom) {
		this.bottom = bottom;
	}

}
