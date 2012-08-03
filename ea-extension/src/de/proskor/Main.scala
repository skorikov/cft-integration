package de.proskor

import de.proskor.extension.AddInBridge
import de.proskor.utils.ExceptionHandler

class Main extends AddInBridge with ExceptionHandler {
  override protected def createExtension = new CftExtension
}