package de.proskor.model.impl;

import cli.EA.IRepository;
import de.proskor.model.Identity;

/**
 * Basic implementation of the identity interface.
 * Every element is peered with a EA peer.
 * @param <T> type of the peer element.
 */
abstract class IdentityImpl<T> implements Identity {
	/** Repository instance. */
	private final IRepository repository;

	/** ID of the element. */
	private final int id;

	/**
	 * Constructor.
	 * @param repository EA repository.
	 * @param id id of the element.
	 */
	protected IdentityImpl(IRepository repository, int id) {
		this.repository = repository;
		this.id = id;
	}

	/**
	 * Get the peer element.
	 * Subclasses should override this.
	 * @return the associated peer element.
	 */
	protected abstract T getPeer();

	/**
	 * Get the IRepository.
	 * @return IRepository instance.
	 */
	protected IRepository getRepository() {
		return this.repository;
	}

	/**
	 * Get the element ID.
	 * @return id of the element.
	 */
	@Override
	public int getId() {
		return this.id;
	}
}
