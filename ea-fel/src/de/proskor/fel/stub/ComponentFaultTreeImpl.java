package de.proskor.fel.stub;

import de.proskor.fel.view.ArchitecturalView;
import de.proskor.fel.view.ComponentFaultTree;

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
