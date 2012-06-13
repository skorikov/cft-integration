package de.proskor.automation.impl.collections

import de.proskor.automation._
import de.proskor.automation.impl._
import cli.EA.ICollection
import cli.EA.IElement

private[automation] class ElementCollection(peer: ICollection) extends AbstractCollection[Element](peer) {
  type PeerType = IElement
  override def create(peer: IElement): Element = new ElementImpl(peer)
  override def update(peer: IElement): Unit = peer.Update()
  override def matches(element: Element, peer: IElement) = element.id == peer.get_ElementID
}