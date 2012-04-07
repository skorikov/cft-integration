package de.proskor.automation.impl

import de.proskor.automation.Node
import de.proskor.automation.Diagram
import de.proskor.automation.Element
import de.proskor.automation.Repository
import cli.EA.IDiagramObject

class NodeImpl(peer: IDiagramObject) extends Node {
  override def id: Int = peer.get_InstanceID

  override def left = peer.get_left
  override def left_=(left: Int) {
    peer.set_right(peer.get_right - peer.get_left + left)
    peer.set_left(left)
    peer.Update()
  }

  override def top = -peer.get_top
  override def top_=(top: Int) {
    peer.set_bottom(peer.get_bottom - peer.get_top - top)
    peer.set_top(-top)
    peer.Update()
  }

  override def width = peer.get_right - peer.get_left
  override def width_=(width: Int) {
    peer.set_right(peer.get_left + width)
    peer.Update()
  }

  override def height = peer.get_top - peer.get_bottom
  override def height_=(height: Int) {
    peer.set_bottom(peer.get_top - height)
    peer.Update()
  }

  override def diagram: Diagram = new DiagramImpl(RepositoryImpl.getDiagramById(peer.get_DiagramID))
  override def element: Element = new ElementImpl(RepositoryImpl.getElementById(peer.get_ElementID))

  override def equals(that: Any): Boolean = that match {
    case node: Node => id == node.id
    case _ => false
  }

  override def hashCode: Int = id
}