package de.proskor.automation.impl

import de.proskor.automation.Element
import de.proskor.automation.Repository
import de.proskor.automation.Connector
import cli.EA.IConnector

class ConnectorImpl(peer: IConnector) extends Connector {
  override def id: Int = peer.get_ConnectorID
  override def guid: String = peer.get_ConnectorGUID.asInstanceOf[String]

  override def name: String = peer.get_Name.asInstanceOf[String]
  override def name_=(name: String): Unit = peer.set_Name(name)

  override def stereotype: String = peer.get_Stereotype.asInstanceOf[String]
  override def stereotype_=(stereotype: String) {
    peer.set_Stereotype(stereotype)
    if (peer.get_SupplierID > 0 && peer.get_ClientID > 0) {
      peer.Update()
      RepositoryImpl.peer.AdviseConnectorChange(peer.get_ConnectorID)
    }
  }

  override def source: Element = new ElementImpl(RepositoryImpl.getElementById(peer.get_ClientID))
  override def source_=(element: Element) {
    peer.set_ClientID(element.id)
    if (peer.get_SupplierID > 0) {
      peer.Update()
      RepositoryImpl.peer.AdviseConnectorChange(peer.get_ConnectorID)
    }
  }

  override def target: Element = new ElementImpl(RepositoryImpl.getElementById(peer.get_SupplierID))
  override def target_=(element: Element) {
    peer.set_SupplierID(element.id)
    if (peer.get_ClientID > 0) {
      peer.Update()
      RepositoryImpl.peer.AdviseConnectorChange(peer.get_ConnectorID)
    }
  }

  override def equals(that: Any): Boolean = that match {
    case connector: Connector => id == connector.id
    case _ => false
  }

  override def hashCode: Int = id
}