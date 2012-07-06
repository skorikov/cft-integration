package de.proskor.fel.impl

import de.proskor.fel.view.View
import de.proskor.automation.Diagram

class ViewImpl(diagram: Diagram) extends View {
	override def getName = diagram.name + " (" + diagram.id + ")"
}