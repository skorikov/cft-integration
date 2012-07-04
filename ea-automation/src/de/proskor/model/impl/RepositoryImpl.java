package de.proskor.model.impl;

import java.util.HashMap;
import java.util.Map;

import cli.EA.ICollection;
import cli.EA.IElement;
import cli.EA.IPackage;
import cli.EA.IRepository;
import de.proskor.model.Collection;
import de.proskor.model.Element;
import de.proskor.model.Package;
import de.proskor.model.Repository;

/**
 * Repository implementation based on the Automation Interface.
 * Stores a reference to the repository peer and delegates operation calls to it.
 */
public class RepositoryImpl implements Repository {
	/** Repository peer. */
	private IRepository peer = null;

	/** Models cache. */
	private Collection<Package> models = null;

	/** Global packages cache. */
	private Map<Integer, Package> packageCache = new HashMap<Integer, Package>();

	/** Global elements cache. */
	private Map<Integer, Element> elementCache = new HashMap<Integer, Element>();

	/**
	 * Represents an output tab in EA.
	 * Multiple output tabs can be created.
	 */
	private class OutputTabImpl implements OutputTab {
		/** Tab name. */
		private String name = null;

		/**
		 * Constructor.
		 * Store the tab name.
		 */
		public OutputTabImpl(String name) {
			this.name = name;
			RepositoryImpl.this.peer.CreateOutputTab(this.name);
		}

		@Override
		public void write(String text) {
			RepositoryImpl.this.peer.EnsureOutputVisible(this.name);
			RepositoryImpl.this.peer.WriteOutput(this.name, text, 0);
		}

		@Override
		public void clear() {
			RepositoryImpl.this.peer.ClearOutput(this.name);
		}

		public void close() {
			RepositoryImpl.this.peer.RemoveOutputTab(this.name);
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
		return new OutputTabImpl(name);
	}

	@Override
	public Collection<Package> getModels() {
		if (this.models == null) {
			final ICollection models = (ICollection) this.peer.get_Models();
			this.models = new CollectionImpl<Package, IPackage>(models) {
				@Override
				protected boolean matches(IPackage object, Package element) {
					return object.get_PackageID() == element.getId();
				}
	
				@Override
				protected Package create(ICollection collection, IPackage element) {
					element.Update();
					collection.Refresh();
					return new PackageImpl(element, null, RepositoryImpl.this);
				}
			};
		}

		return this.models;
	}

	Element getElementById(int id) {
		final Element cached = this.elementCache.get(id);
		if (cached != null)
			return cached;

		final IElement element = (IElement) this.peer.GetElementByID(id);

		Element parent = null;
		final int parentId = element.get_ParentID();
		if (parentId != 0)
			parent = this.getElementById(parentId);

		final Package pkg = this.getPackageById(element.get_PackageID());
		final Element result = new ElementImpl(element, parent, pkg);
		this.elementCache.put(id, result);

		return result;
	}

	Package getPackageById(int id) {
		final Package cached = this.packageCache.get(id);
		if (cached != null)
			return cached;

		final IPackage pkg = (IPackage) this.peer.GetPackageByID(id);

		Package parent = null;
		final int parentId = pkg.get_ParentID();
		if (parentId != 0)
			parent = this.getPackageById(parentId);

		final Package result = new PackageImpl(pkg, parent, this);
		this.packageCache.put(id, result);

		return result;
	}
}
