package de.proskor.automation

import cli.EA.IConnector

class Connector(peer: IConnector) {
  def id: Int = peer.get_ConnectorID
  def source: Element = new Element(Repository.getElementById(peer.get_ClientID))
  def target: Element = new Element(Repository.getElementById(peer.get_SupplierID))

  override def equals(that: Any): Boolean = that match {
    case connector: Connector => id == connector.id
    case _ => false
  }

  override def hashCode: Int = id
}