package de.proskor.ea.model.fel
import de.proskor.model.fel.FailureMode
import de.proskor.model.Element

abstract class EAFailureMode(val element: Element, var name: String, var description: String ="", var author: String = "") extends FailureMode {

}