package de.proskor.automation.impl

import de.proskor.automation.Element
import de.proskor.automation.Repository
import de.proskor.automation.Connector
import cli.EA.IConnector

class ConnectorImpl(peer: IConnector) extends Connector {
  override def id: Int = peer.get_ConnectorID

  override def name: String = peer.get_Name.asInstanceOf[String]
  override def name_=(name: String): Unit = peer.set_Name(name)

  override def stereotype: String = peer.get_Stereotype.asInstanceOf[String]
  override def stereotype_=(stereotype: String): Unit = peer.set_Stereotype(stereotype)

  override def source: Element = new ElementImpl(RepositoryImpl.getElementById(peer.get_ClientID))
  override def target: Element = new ElementImpl(RepositoryImpl.getElementById(peer.get_SupplierID))

  override def equals(that: Any): Boolean = that match {
    case connector: Connector => id == connector.id
    case _ => false
  }

  override def hashCode: Int = id
}