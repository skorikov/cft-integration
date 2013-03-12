package de.proskor.integration;

import java.util.HashMap;
import java.util.Map;

import de.proskor.integration.model.Source;
import de.proskor.integration.model.Target;

abstract class MappingContext {
	private final Map<Source, Source> sources = new HashMap<Source, Source>();
	private final Map<Target, Target> targets = new HashMap<Target, Target>();

	protected final void put(Source element, Source copy) {
		this.sources.put(element, copy);
	}

	protected final void put(Target element, Target copy) {
		this.targets.put(element, copy);
	}

	protected final Source get(Source element) {
		return this.sources.get(element);
	}

	protected final Target get(Target element) {
		return this.targets.get(element);
	}
}
