package de.proskor.automation.impl.collections

import de.proskor.automation._
import de.proskor.automation.impl._
import cli.EA.ICollection
import cli.EA.ITaggedValue

private[automation] class TaggedValueCollection(peer: ICollection) extends AbstractCollection[TaggedValue](peer) {
  type PeerType = ITaggedValue
  override def create(peer: ITaggedValue): TaggedValue = new TaggedValueImpl(peer)
  override def update(peer: ITaggedValue): Unit = peer.Update()
  override def matches(value: TaggedValue, peer: ITaggedValue) = value.id == peer.get_ElementID
}