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
  protected def getElement(element: cli.EA.IElement): Element = element.get_Stereotype match {
    case "Event" =>  new EAEvent(element, repository)
    case "Input" => new EAInput(element, repository)
    case "Output" => new EAOutput(element, repository)
    case "OR" => new EAOr(element, repository)
    case "AND" => new EAAnd(element, repository)
    case _ => new EAElement(element, repository)
  }
  def targets = targetElements.map(getElement).toList
}