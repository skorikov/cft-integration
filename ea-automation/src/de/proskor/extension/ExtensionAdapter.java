package de.proskor.extension;

import de.proskor.model.Diagram;
import de.proskor.model.Element;
import de.proskor.model.Package;
import de.proskor.model.Repository;

/**
 * A dummy implementation of the Extension interface.
 * Clients should refine this class and override the required methods.
 */
public abstract class ExtensionAdapter implements Extension {
	/** Stores the menu. */
	private MenuItem menu = null;

	/** Store the repository. */
	private Repository repository = null;

	/**
	 * Create the menu.
	 * This method is called when the extension is initialized.
	 * Clients should override this method.
	 */
	protected abstract MenuItem createMenu();

	/**
	 * Get the current repository.
	 * @return the current repository.
	 */
	protected final Repository getRepository() {
		return this.repository;
	}

	/** NOP */
	@Override
	public void start() {}

	/**
	 * Store the repository reference.
	 * Clients should obtain the repository by calling the getReposotory method.
	 */
	@Override
	public void initialize(Repository repository) {
		this.repository = repository;
	}

	/** NOP */
	@Override
	public void stop() {}

	/**
	 * Get the menu.
	 */
	@Override
	public final MenuItem getMenu() {
		if (this.menu == null)
			this.menu = this.createMenu();

		return this.menu;
	}

	/** NOP */
	@Override
	public boolean deleteElement(Element element) {
		return true;
	}

	/** NOP */
	@Override
	public boolean deletePackage(Package pkg) {
		return true;
	}

	/** NOP */
	@Override
	public boolean deleteDiagram(Diagram diagram) {
		return true;
	}
}
