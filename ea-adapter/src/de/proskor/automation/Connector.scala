package de.proskor.automation

import cli.EA.IConnector

class Connector(peer: IConnector) {
  def source: Element = new Element(Repository.getElementById(peer.get_ClientID))
  def target: Element = new Element(Repository.getElementById(peer.get_SupplierID))
}