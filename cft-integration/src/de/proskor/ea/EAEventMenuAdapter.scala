package de.proskor.ea

import de.proskor.fel.ui.FailureEventListImpl
import de.proskor.ea.model.EAElement
import cli.EA.IRepository
import cli.EA.IElement

class EAEventMenuAdapter {
  private val fel: FailureEventListImpl = null

  def getSelectedElement(repository: IRepository): EAElement =
    new EAElement(repository.GetContextObject.asInstanceOf[IElement], repository)
	
  def newEventTypeMenu_isEnabled(repository: IRepository): Boolean =
    EAConstants.stereotypeComponent == getSelectedElement(repository).stereotype
	
  def newEventInstanceMenu_isEnabled(repository: IRepository): Boolean =
    EAConstants.stereotypeCFT == getSelectedElement(repository).stereotype
	
  def createEventType(repository: IRepository) {
    fel.reloadRepository()
    fel.createEventType() // User kann hier ein neues Element erzeugen.
  }
	
  def createEventInstance(repository: IRepository) {
    fel.reloadRepository()
    fel.createEventInstance() // User kann hier ein neues Element erzeugen.
  }
}