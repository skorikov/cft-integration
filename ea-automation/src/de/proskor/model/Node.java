package de.proskor.model;

public interface Node {
	int getId();

	int getLeft();
	void setLeft(int left);

	int getTop();
	void setTop(int top);

	int getRight();
	void setRight(int right);

	int getBottom();
	void setBottom(int bottom);

	int getSequence();
	void setSequence(int sequence);

	Diagram getDiagram();
	Element getElement();
}
