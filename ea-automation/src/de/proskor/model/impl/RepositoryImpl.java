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

	private class Output implements Writable {
		/** Output tab. */
		private final static String TAB = "OUT";

		/**
		 * Constructor.
		 */
		public Output() {
			RepositoryImpl.this.getPeer().CreateOutputTab(Output.TAB);
			RepositoryImpl.this.getPeer().EnsureOutputVisible(Output.TAB);
		}

		@Override
		public void write(String text) {
			RepositoryImpl.this.getPeer().WriteOutput(Output.TAB, text, 0);
		}

		@Override
		public void clear() {
			RepositoryImpl.this.getPeer().ClearOutput(Output.TAB);
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
			this.output = new Output();

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
