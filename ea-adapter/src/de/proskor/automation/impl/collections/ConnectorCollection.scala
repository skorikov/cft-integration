package de.proskor.automation.impl.collections

import de.proskor.automation.Connector
import de.proskor.automation.impl.ConnectorImpl
import cli.EA.ICollection
import cli.EA.IConnector

private[automation] class ConnectorCollection(peer: ICollection) extends AbstractCollection[Connector](peer) {
  type PeerType = IConnector
  override def create(peer: IConnector): Connector = {
    peer.set_Direction("Source -> Destination")
    new ConnectorImpl(peer)
  }
  override def update(peer: IConnector) {}
  override def matches(element: Connector, peer: IConnector) = element.id == peer.get_ConnectorID
}