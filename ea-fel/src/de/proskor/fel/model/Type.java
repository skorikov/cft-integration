package de.proskor.fel.model;

import java.util.List;


public interface Type extends Entity {
	public List<? extends Instance> getInstances();
	public String getName();
}
