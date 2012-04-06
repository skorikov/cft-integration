package de.proskor.automation.collections

import de.proskor.automation.Repository
import cli.EA.ICollection

abstract class Collection[T](peer: ICollection) extends Iterable[T] {
  type PeerType

  override def size: Int = peer.get_Count
  override def isEmpty: Boolean = peer.get_Count == 0

  def add(name: String, typ: String): T = {
    val result = peer.AddNew(name, typ).asInstanceOf[PeerType]
    update(result)
    peer.Refresh()
    Repository.peer.RefreshModelView(0)
    create(result)
  }

  def delete(element: T) {
    val index: Int = indexOf(element)
    if (index >= 0) deleteAt(index)
  }

  def deleteAt(index: Int) {
    peer.Delete(index.toShort)
    peer.Refresh()
    Repository.peer.RefreshModelView(0)
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

  def getAt(index: Int): T = {
    create(peer.GetAt(index.toShort).asInstanceOf[PeerType])
  }

  def clear() {
    for (i <- 0 until peer.get_Count) {
      peer.Delete(i.toShort)
    }
    peer.Refresh()
    Repository.peer.RefreshModelView(0)
  }

  override def iterator: Iterator[T] = new Iterator[T] {
    private var current: Int = -1
    override def hasNext: Boolean = (current + 1) < Collection.this.size
    override def next: T = {
      current += 1
      getAt(current)
    }
  }

  protected def create(peer: PeerType): T
  protected def update(peer: PeerType): Unit
  protected def matches(element: T, peer: PeerType): Boolean
}