package de.proskor.ea.model.cft
import de.proskor.ea.model.EATraversable._
import de.proskor.ea.model.EAElement
import de.proskor.ea.model.EADiagram
import de.proskor.model.cft.Gate
import de.proskor.model.Element
import de.proskor.model.cft.Source


abstract class EAGate(element: cli.EA.IElement, repository: cli.EA.IRepository) extends EAElement(element, repository) with Gate {
  protected def connectedElements = {
    val collection: Traversable[cli.EA.IConnector] = element.get_Connectors.asInstanceOf[cli.EA.Collection]
    for (connector <- collection if connector.get_SupplierID == element.get_ElementID && connector.get_Stereotype == "Connection")
      yield repository.GetElementByID(connector.get_ClientID).asInstanceOf[cli.EA.IElement]
  }

  protected def targetElements = {
    val collection: Traversable[cli.EA.IConnector] = element.get_Connectors.asInstanceOf[cli.EA.Collection]
    for (connector <- collection if connector.get_ClientID == element.get_ElementID && connector.get_Stereotype == "Connection")
      yield repository.GetElementByID(connector.get_SupplierID).asInstanceOf[cli.EA.IElement]
  }

/*  protected def getElement(element: cli.EA.IElement): Element = element.get_Stereotype match {
    case "Event" =>  new EAEvent(element, repository)
    case "Input" => new EAInput(element, repository)
    case "Output" => new EAOutput(element, repository)
    case "OR" => new EAOr(element, repository)
    case "AND" => new EAAnd(element, repository)
    case _ => new EAElement(element, repository)
  }*/

  def sources = connectedElements.map(getElement).asInstanceOf[List[Source]]

  def targets = targetElements.map(getElement).toList

  def +=(source: Source) {
    val connector = element.get_Connectors.asInstanceOf[cli.EA.Collection].AddNew("", "Connector").asInstanceOf[cli.EA.IConnector]
    connector.set_Stereotype("Connection")
    connector.set_SupplierID(element.get_ElementID)
    connector.set_ClientID(source.asInstanceOf[EAElement].element.get_ElementID)
    connector.set_Direction("Source -> Destination")
    connector.Update()
  }
}