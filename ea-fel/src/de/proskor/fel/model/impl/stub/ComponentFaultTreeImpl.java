package de.proskor.fel.model.impl.stub;

import de.proskor.fel.model.view.ArchitecturalView;
import de.proskor.fel.model.view.ComponentFaultTree;

public class ComponentFaultTreeImpl extends ViewImpl implements ComponentFaultTree {
	private ArchitecturalView context;

	@Override
	public ArchitecturalView getContext() {
		return context;
	}

	public void setContext(ArchitecturalView context) {
		this.context = context;
	}
}
