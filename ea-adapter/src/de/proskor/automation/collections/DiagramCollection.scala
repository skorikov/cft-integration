package de.proskor.automation.collections

import de.proskor.automation.Diagram
import cli.EA.ICollection
import cli.EA.IDiagram

class DiagramCollection(peer: ICollection) extends Collection[Diagram](peer) {
  type PeerType = IDiagram
  override def create(peer: IDiagram): Diagram = new Diagram(peer)
  override def update(peer: IDiagram): Unit = peer.Update()
}