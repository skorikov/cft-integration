package de.proskor.automation.impl.collections

import de.proskor.automation._
import de.proskor.automation.impl._
import cli.EA.ICollection

private[automation] abstract class AbstractCollection[T](peer: ICollection) extends Collection[T] {
  type PeerType

  override def size: Int = peer.get_Count
  override def isEmpty: Boolean = peer.get_Count == 0

  override def add(name: String, typ: String): T = {
    val result = peer.AddNew(name, typ).asInstanceOf[PeerType]
    update(result)
    peer.Refresh()
    RepositoryImpl.peer.RefreshModelView(0)
    create(result)
  }

  override def remove(element: T) {
    val index: Int = indexOf(element)
    if (index >= 0) removeAt(index)
  }

  def removeAt(index: Int) {
    peer.Delete(index.toShort)
    peer.Refresh()
    RepositoryImpl.peer.RefreshModelView(0)
  }

  def indexOf(element: T): Int = {
    var current: Int = 0
    val count = peer.get_Count
    while (current < count) {
      if (matches(element, peer.GetAt(current.toShort).asInstanceOf[PeerType]))
        return current;
      else
        current += 1;
    }
    -1
  }

  def getAt(index: Int): T =
    create(peer.GetAt(index.toShort).asInstanceOf[PeerType])

  override def clear() {
    for (i <- 0 until peer.get_Count) {
      peer.Delete(i.toShort)
    }
    peer.Refresh()
    RepositoryImpl.peer.RefreshModelView(0)
  }

  override def contains(element: T): Boolean = indexOf(element) >= 0

  override def iterator: Iterator[T] = new Iterator[T] {
    peer.Refresh()
    private var current: Int = -1
    override def hasNext: Boolean = (current + 1) < AbstractCollection.this.size
    override def next: T = {
      current += 1
      getAt(current)
    }
  }

  protected def create(peer: PeerType): T
  protected def update(peer: PeerType): Unit
  protected def matches(element: T, peer: PeerType): Boolean
}