package de.proskor.automation

import cli.EA.IDiagramObject

class Node(peer: IDiagramObject) {
  def left = peer.get_left
  def left_=(left: Int) {
    peer.set_right(peer.get_right - peer.get_left + left)
    peer.set_left(left)
    peer.Update()
  }

  def top = -peer.get_top
  def top_=(top: Int) {
    peer.set_bottom(peer.get_bottom - peer.get_top - top)
    peer.set_top(-top)
    peer.Update()
  }

  def width = peer.get_right - peer.get_left
  def width_=(width: Int) {
    peer.set_right(peer.get_left + width)
    peer.Update()
  }

  def height = peer.get_top - peer.get_bottom
  def height_=(height: Int) {
    peer.set_bottom(peer.get_top - height)
    peer.Update()
  }

  def diagram: Diagram = new Diagram(Repository.getDiagramById(peer.get_DiagramID))
  def element: Element = new Element(Repository.getElementById(peer.get_ElementID))
}