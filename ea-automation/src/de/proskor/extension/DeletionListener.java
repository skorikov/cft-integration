package de.proskor.extension;

import de.proskor.model.Diagram;
import de.proskor.model.Element;
import de.proskor.model.Package;

/**
 * An observer for deletion events.
 */
public interface DeletionListener {
	/**
	 * Called when an element is about to be deleted.
	 * @param element element to be deleted.
	 * @return true if the deletion should be performed, false otherwise.
	 */
	boolean deleteElement(Element element);

	/**
	 * Called when a package is about to be deleted.
	 * @param pkg package to be deleted.
	 * @return true if the deletion should be performed, false otherwise.
	 */
	boolean deletePackage(Package pkg);

	/**
	 * Called when a diagram is about to be deleted.
	 * @param diagram diagram to be deleted.
	 * @return true if the deletion should be performed, false otherwise.
	 */
	boolean deleteDiagram(Diagram diagram);
}
