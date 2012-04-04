package de.proskor.automation.collections

import de.proskor.automation.Node
import cli.EA.ICollection
import cli.EA.IDiagramObject

class NodeCollection(peer: ICollection) extends Collection[Node](peer) {
  type PeerType = IDiagramObject
  override def create(peer: IDiagramObject): Node = new Node(peer)
  override def update(peer: IDiagramObject): Unit = peer.Update()
}