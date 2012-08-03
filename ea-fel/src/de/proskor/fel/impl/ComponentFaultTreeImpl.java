package de.proskor.fel.impl;

import de.proskor.fel.view.ArchitecturalView;
import de.proskor.fel.view.ComponentFaultTree;
import de.proskor.model.Diagram;
import de.proskor.model.Package;
import de.proskor.model.Repository;
import de.proskor.model.TaggedValue;

public class ComponentFaultTreeImpl extends ViewImpl implements ComponentFaultTree {
	public ComponentFaultTreeImpl(Repository repository, Diagram peer) {
		super(repository, peer);
	}

	@Override
	public ArchitecturalView getContext() {
		final TaggedValue tv = this.getContextAssociation();
		if (tv != null) {
			final Repository repository = this.getRepository();
			final String[] values = tv.getValue().split(":isContextFor:");
			final Diagram context = repository.getDiagramById(Integer.valueOf(values[0]));
			return new ArchitecturalViewImpl(repository, context);
		}
		return null;
	}

	@Override
	public void setContext(ArchitecturalView view) {
		if (!(view instanceof ArchitecturalViewImpl))
			throw new IllegalArgumentException();

		final String value = ((ArchitecturalViewImpl) view).getPeer().getId() + ":isContextFor:" + this.getPeer().getId();
		final TaggedValue tv = this.getContextAssociation();
		if (tv != null) {
			tv.setValue(value);
		} else {
			final TaggedValue nt = this.getFEL().getElement().getTaggedValues().add("context", TaggedValue.TAGGEDVALUE);
			nt.setValue(value);
			
		}
	}

	private Package getFEL() {
		final Repository repository = this.getRepository();
		final Package model = repository.getModels().get(0);
		for (final Package pkg : model.getPackages()) {
			if (pkg.getName().equals("FEL"))
				return pkg;
		}
		return model.getPackages().add("FEL", Package.PACKAGE);
	}

	private TaggedValue getContextAssociation() {
		final Package fel = this.getFEL();
		final Repository repository = this.getRepository();
		final Diagram peer = this.getPeer();
		for (final TaggedValue tv : fel.getElement().getTaggedValues()) {
			if (tv.getName().equals("context")) {
				final String[] values = tv.getValue().split(":isContextFor:");
				final Diagram cft = repository.getDiagramById(Integer.valueOf(values[1]));
				if (peer.equals(cft))
					return tv;
			}
		}
		return null;
	}
}
