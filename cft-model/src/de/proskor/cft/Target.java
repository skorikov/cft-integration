package de.proskor.cft;

import java.util.Collection;

public interface Target extends Source {
	Collection<Source> getInputs();
}
