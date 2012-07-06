package de.proskor.model;

public interface Node {
	int getId();

	int getLeft();
	void setLeft(int left);

	int getTop();
	void setTop(int top);

	int getWidth();
	void setWidth(int width);

	int getHeight();
	void setHeight(int height);

	int getSequence();
	void setSequence(int sequence);

	Diagram getDiagram();
	Element getElement();
}
