package de.proskor.cft.model.ea.peers
import de.proskor.cft.model.ea._

class EAElementPeer(var instance: cli.EA.IElement) extends EAPeer {
  val id: Int = instance.get_ElementID

  def name = instance.get_Name.asInstanceOf[String]
  def name_=(name: String) = instance.set_Name(name)

  def stereotype: String = instance.get_Stereotype.asInstanceOf[String]
  def stereotype_=(stereotype: String) = instance.set_Stereotype(stereotype)

  private def kids: Set[cli.EA.IElement] = {
    val collection = instance.get_Elements.asInstanceOf[cli.EA.ICollection]
    val instances = for (i <- 0 until collection.get_Count) yield collection.GetAt(i.toShort).asInstanceOf[cli.EA.IElement]
    instances.toSet
  }

  def packages: Set[EAPeer] = Set()

  def elements: Set[EAPeer] =
    for (kid <- kids) yield new EAElementPeer(kid)

  def elementsOfType(stereotypes: String*): Set[EAPeer] =
    for (kid <- kids if stereotypes.contains(kid.get_Stereotype)) yield new EAElementPeer(kid)

  def addElement(name: String, stereotype: String): EAPeer = {
    val collection = instance.get_Elements.asInstanceOf[cli.EA.ICollection]
    val element = collection.AddNew(name, "Object").asInstanceOf[cli.EA.IElement]
    element.set_Stereotype(stereotype)
    element.Update()
    collection.Refresh()
    EAFactory.cache += element.get_ElementID -> element
    new EAElementPeer(element)
  }

  def parent: EAPeer = {
    if (instance.get_ParentID > 0)
      new EAElementPeer(EAFactory.element(instance.get_ParentID))
    else
      new EAPackagePeer(EAFactory.pkg(instance.get_PackageID))
  }

  def deleteElement(element: EAPeer) {
    val collection = instance.get_Elements.asInstanceOf[cli.EA.ICollection]
    var i = 0
    var found = false
    while (i < collection.get_Count && !found) {
      if (collection.GetAt(i.toShort).asInstanceOf[cli.EA.IElement].get_ElementID == element.asInstanceOf[EAElementPeer].instance.get_ElementID)
        found = true
      else
        i += 1
    }
    if (found) {
      collection.Delete(i.toShort)
      collection.Refresh()
      EAFactory.cache -= element.id
    }
  }

  def deletePackage(pkg: EAPeer) {}
  def addPackage(name: String): EAPeer = null

  def connectedElements: Set[EAPeer] = {
    val collection = instance.get_Connectors.asInstanceOf[cli.EA.Collection]
    val instances = for {
      i <- 0 until collection.get_Count
      connector = collection.GetAt(i.toShort).asInstanceOf[cli.EA.IConnector]
      if connector.get_SupplierID == instance.get_ElementID && connector.get_Stereotype == "Connection"
    } yield new EAElementPeer(EAFactory.element(connector.get_ClientID))
    instances.toSet
  }

  def connect(element: EAPeer) {
    val collection = instance.get_Connectors.asInstanceOf[cli.EA.Collection]
    val connector = collection.AddNew("", "Connector").asInstanceOf[cli.EA.IConnector]
    connector.set_Stereotype("Connection")
    connector.set_SupplierID(id)
    connector.set_ClientID(element.id)
    connector.set_Direction("Source -> Destination")
    connector.Update()
    collection.Refresh()
  }

  def disconnect(element: EAPeer) {
    val collection = instance.get_Connectors.asInstanceOf[cli.EA.Collection]
    var i = 0
    var found = false
    while (i < collection.get_Count && !found) {
      val connector = collection.GetAt(i.toShort).asInstanceOf[cli.EA.IConnector]
      if (connector.get_SupplierID == id && connector.get_ClientID == element.id && connector.get_Stereotype == "Connection")
        found = true
      else
        i += 1
    }
    if (found) {
      collection.Delete(i.toShort)
      collection.Refresh()
    }
  }
}