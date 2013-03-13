package de.proskor.cft.merge.ui;

import de.proskor.cft.Container;
import de.proskor.cft.Element;

abstract class ElementWrapper {
	private final Element element;

	public ElementWrapper(Element element) {
		if (element == null)
			throw new IllegalArgumentException("element is null");

		this.element = element;
	}

	public String getName() {
		return this.getName(this.element);
	}

	private String getName(Element element) {
		final Container parent = element.getParent();
		return (parent != null ? this.getName(parent) : "") + "/" + element.getName();
	}
}