package de.proskor.automation

import cli.EA.ICollection
import cli.EA.IConnector

class ConnectorCollection(peer: ICollection) extends Collection[Connector](peer) {
  type PeerType = IConnector
  override def create(peer: IConnector): Connector = new Connector(peer)
  override def update(peer: IConnector): Unit = peer.Update()
}