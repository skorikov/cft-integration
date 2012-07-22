package de.proskor.model.impl;

import java.util.HashMap;
import java.util.Map;

import cli.EA.ICollection;
import cli.EA.IConnector;
import cli.EA.IDiagram;
import cli.EA.IElement;
import cli.EA.IPackage;
import cli.EA.IRepository;
import cli.EA.ObjectType;
import de.proskor.model.Collection;
import de.proskor.model.Diagram;
import de.proskor.model.Element;
import de.proskor.model.Entity;
import de.proskor.model.Package;
import de.proskor.model.Repository;

/**
 * Repository implementation based on the Automation Interface.
 * Stores a reference to the repository peer and delegates operation calls to it.
 * TODO: Fix the caching problem!
 */
public class RepositoryImpl implements Repository {
	/** Repository peer. */
	private IRepository peer = null;

	/** Output tabs. */
	private Map<String, OutputTab> tabs = new HashMap<String, OutputTab>();

	/**
	 * Represents an output tab in EA.
	 * Multiple output tabs can be created.
	 */
	private class OutputTabImpl implements OutputTab {
		/** Tab name. */
		private String name = null;

		/** Tab status. */
		private boolean closed = false;

		/**
		 * Constructor.
		 * Store the tab name.
		 */
		public OutputTabImpl(String name) {
			this.name = name;
			RepositoryImpl.this.peer.CreateOutputTab(this.name);
		}

		/**
		 * Write to the output.
		 */
		@Override
		public void write(String text) {
			if (this.closed)
				throw new IllegalStateException("Output tab '" + this.name + "' has been closed.");

			RepositoryImpl.this.peer.EnsureOutputVisible(this.name);
			RepositoryImpl.this.peer.WriteOutput(this.name, text, 0);
		}

		/**
		 * Clear the output tab.
		 */
		@Override
		public void clear() {
			if (this.closed)
				throw new IllegalStateException("Output tab '" + this.name + "' has been closed.");			

			RepositoryImpl.this.peer.ClearOutput(this.name);
		}

		/**
		 * Close the output tab.
		 * Subsequent write calls will result in exceptions thrown.
		 */
		public void close() {
			this.closed = true;
			RepositoryImpl.this.peer.RemoveOutputTab(this.name);
			RepositoryImpl.this.tabs.remove(this.name);
		}
	}

	/**
	 * Constructor.
	 * @param peer repository peer.
	 */
	public RepositoryImpl(IRepository peer) {
		this.peer = peer;
	}

	/**
	 * Get output.
	 * @return output.
	 */
	@Override
	public OutputTab getOutputTab(String name) {
		final OutputTab cached = this.tabs.get(name);
		if (cached != null)
			return cached;

		final OutputTab result = new OutputTabImpl(name);
		this.tabs.put(name, result);

		return result;
	}

	@Override
	public Collection<Package> getModels() {
		final ICollection collection = (ICollection) this.peer.get_Models();
		return new CollectionImpl<Package, IPackage>(collection) {
			@Override
			protected boolean matches(IPackage object, Package element) {
				return object.get_PackageID() == element.getId();
			}

			@Override
			protected Package create(IPackage element) {
				element.Update();
				return new PackageImpl(RepositoryImpl.this.peer, element.get_PackageID());
			}
		};
	}

	public Element getElementById(int id) {
		final IElement element = (IElement) this.peer.GetElementByID(id);

		if (element == null)
			throw new IllegalArgumentException();

		return new ElementImpl(this.peer, id);
	}

	public Package getPackageById(int id) {
		final IPackage pkg = (IPackage) this.peer.GetPackageByID(id);

		if (pkg == null)
			throw new IllegalArgumentException();

		return new PackageImpl(this.peer, id);
	}

	@Override
	public Diagram getDiagramById(int id) {
		final IDiagram diagram = (IDiagram) this.peer.GetDiagramByID(id);

		if (diagram == null)
			throw new IllegalArgumentException();

		return new DiagramImpl(this.peer, id);
	}

	@Override
	public Entity getContext() {
		final ObjectType type = this.peer.GetContextItemType();
		switch (type.Value) {
		case ObjectType.otElement:
			final int elementId = ((IElement) this.peer.GetContextObject()).get_ElementID();
			return new ElementImpl(this.peer, elementId);
		case ObjectType.otPackage:
			final int packageId = ((IPackage) this.peer.GetContextObject()).get_PackageID();
			return new PackageImpl(this.peer, packageId);
		case ObjectType.otDiagram:
			final int diagramId = ((IDiagram) this.peer.GetContextObject()).get_DiagramID();
			return new DiagramImpl(this.peer, diagramId);
		case ObjectType.otConnector:
			final int connectorId = ((IConnector) this.peer.GetContextObject()).get_ConnectorID();
			return new ConnectorImpl(this.peer, connectorId);
		default:
			return null;
		}
	}

	@Override
	public String toString() {
		return "REPOSITORY[" + this.peer.get_ConnectionString() + "]";
	}

	@Override
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (that instanceof RepositoryImpl)
			return this.peer.get_ConnectionString().equals(((RepositoryImpl) that).peer.get_ConnectionString());

		return false;
	}

	@Override
	public int hashCode() {
		return this.peer.get_ConnectionString().hashCode();
	}
}
