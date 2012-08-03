package de.proskor.model;

public interface TaggedValue extends Entity {
	String TAGGEDVALUE = "";

	/**
	 * Get the value of the tagged value.
	 * @return value of the tagged value.
	 */
	String getValue();

	/**
	 * Set the value of the tagged value.
	 * @param name new value.
	 */
	void setValue(String value);

	/**
	 * Get the associated element.
	 * @return associated element.
	 */
	Element getElement();
}
