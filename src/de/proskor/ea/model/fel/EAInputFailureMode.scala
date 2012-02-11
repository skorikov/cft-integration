package de.proskor.ea.model.fel
import de.proskor.model.fel.InputFailureMode
import de.proskor.model.Element

class EAInputFailureMode(element: Element, name: String, description: String, author: String) extends EAFailureMode(element, name, description, author) with InputFailureMode