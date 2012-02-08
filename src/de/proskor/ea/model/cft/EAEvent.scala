package de.proskor.ea.model.cft
import de.proskor.ea.model.EATraversable._
import de.proskor.ea.model.EAElement
import de.proskor.model.cft.Event
import de.proskor.model.Element

class EAEvent(element: cli.EA.IElement, repository: cli.EA.IRepository) extends EAElement(element, repository) with Event {
  protected def targetElements = {
    val collection: Traversable[cli.EA.IConnector] = element.get_Connectors.asInstanceOf[cli.EA.Collection]
    for (connector <- collection if connector.get_ClientID == element.get_ElementID && connector.get_Stereotype == "Connection")
      yield repository.GetElementByID(connector.get_SupplierID).asInstanceOf[cli.EA.IElement]
  }
  def targets = targetElements.map(getElement).toList
}