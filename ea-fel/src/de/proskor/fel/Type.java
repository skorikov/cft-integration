package de.proskor.fel;

import java.util.List;

public interface Type extends Entity {
	public List<? extends Instance> getInstances();
	public String getName();
}
