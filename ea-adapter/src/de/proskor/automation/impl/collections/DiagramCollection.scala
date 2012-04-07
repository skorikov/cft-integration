package de.proskor.automation.impl.collections

import de.proskor.automation.Diagram
import de.proskor.automation.impl.DiagramImpl
import cli.EA.ICollection
import cli.EA.IDiagram

private[automation] class DiagramCollection(peer: ICollection) extends AbstractCollection[Diagram](peer) {
  type PeerType = IDiagram
  override def create(peer: IDiagram): Diagram = new DiagramImpl(peer)
  override def update(peer: IDiagram): Unit = peer.Update()
  override def matches(element: Diagram, peer: IDiagram) = element.id == peer.get_DiagramID
}