package de.proskor.model.impl;

import cli.EA.IRepository;
import de.proskor.model.Collection;
import de.proskor.model.Model;
import de.proskor.model.Repository;
import de.proskor.model.Writable;

/**
 * Repository implementation based on the Automation Interface.
 * Stores a reference to the repository peer and delegates operation calls to it.
 */
public class RepositoryImpl implements Repository {
	/** Repository peer. */
	private IRepository peer = null;

	/** Output. */
	private Writable output = null;

	/** Default output tab. */
	private final static String TAB = "OUT";

	/**
	 * Represents an output tab in EA.
	 * Multiple output tabs can be created.
	 */
	private class Output implements Writable {
		/** Tab name. */
		private String tab = null;

		/**
		 * Constructor.
		 * Store the tab name.
		 */
		public Output(String tab) {
			this.tab = tab;
			RepositoryImpl.this.getPeer().CreateOutputTab(tab);
			RepositoryImpl.this.getPeer().EnsureOutputVisible(tab);
		}

		@Override
		public void write(String text) {
			RepositoryImpl.this.getPeer().WriteOutput(this.tab, text, 0);
		}

		@Override
		public void clear() {
			RepositoryImpl.this.getPeer().ClearOutput(this.tab);
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
	 * Get repository peer.
	 * @return repository peer.
	 */
	private IRepository getPeer() {
		return this.peer;
	}

	/**
	 * Get output.
	 * @return output.
	 */
	private Writable getOutput() {
		if (this.output == null)
			this.output = new Output(RepositoryImpl.TAB);

		return this.output;
	}

	@Override
	public void write(String text) {
		this.getOutput().write(text);
	}

	@Override
	public void clear() {
		this.getOutput().clear();
	}

	@Override
	public Collection<Model> getModels() {
		// TODO Auto-generated method stub
		return null;
	}

}
