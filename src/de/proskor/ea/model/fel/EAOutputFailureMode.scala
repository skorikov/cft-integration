package de.proskor.ea.model.fel
import de.proskor.model.fel.OutputFailureMode
import de.proskor.model.Element

class EAOutputFailureMode(element: Element, name: String, description: String, author: String) extends EAFailureMode(element, name, description, author) with OutputFailureMode