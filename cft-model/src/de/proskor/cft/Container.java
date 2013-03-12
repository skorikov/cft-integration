package de.proskor.cft;

import java.util.Collection;

public interface Container extends Element {
	Collection<Element> getElements();
}
