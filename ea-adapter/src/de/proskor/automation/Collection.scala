package de.proskor.automation

import cli.EA.ICollection

abstract class Collection[T](peer: ICollection) extends Iterable[T] {
  type PeerType

  override def size: Int = peer.get_Count

  def add(name: String, typ: String): T = {
    val result = peer.AddNew(name, typ).asInstanceOf[PeerType]
    update(result)
    peer.Refresh()
    create(result)
  }

  def deleteAt(index: Int) {
    peer.Delete(index.toShort)
    peer.Refresh()
  }

  def getAt(index: Int): T = {
    create(peer.GetAt(index.toShort).asInstanceOf[PeerType])
  }

  override def iterator: Iterator[T] = new Iterator[T] {
    private var current: Int = 0
    override def hasNext: Boolean = current < Collection.this.size
    override def next: T = {
      current += 1
      getAt(current - 1)
    }
    
  }

  protected def create(peer: PeerType): T
  protected def update(peer: PeerType): Unit
}