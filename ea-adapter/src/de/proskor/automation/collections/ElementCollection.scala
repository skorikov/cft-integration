package de.proskor.automation.collections

import de.proskor.automation.Element
import cli.EA.ICollection
import cli.EA.IElement

class ElementCollection(peer: ICollection) extends Collection[Element](peer) {
  type PeerType = IElement
  override def create(peer: IElement): Element = new Element(peer)
  override def update(peer: IElement): Unit = peer.Update()
  override def matches(element: Element, peer: IElement) = element.id == peer.get_ElementID
}