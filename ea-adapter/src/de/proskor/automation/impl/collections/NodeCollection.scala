package de.proskor.automation.impl.collections

import de.proskor.automation.Node
import de.proskor.automation.impl.NodeImpl
import cli.EA.ICollection
import cli.EA.IDiagramObject

private[automation] class NodeCollection(peer: ICollection) extends AbstractCollection[Node](peer) {
  type PeerType = IDiagramObject
  override def create(peer: IDiagramObject): Node = new NodeImpl(peer)
  override def update(peer: IDiagramObject): Unit = peer.Update()
  override def matches(element: Node, peer: IDiagramObject) = element.asInstanceOf[NodeImpl].peer.get_InstanceID == peer.get_InstanceID
}