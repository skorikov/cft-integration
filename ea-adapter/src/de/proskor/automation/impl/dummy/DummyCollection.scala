package de.proskor.automation.impl.dummy

import de.proskor.automation.Collection

class DummyCollection[T, P](parent: P, create: (String, String, P) => T) extends Collection[T] {
  protected var contents: Set[T] = Set()

  override def add(name: String, typ: String): T = {
    val element = create(name, typ, parent)
    contents += element
    element
  }

  override def remove(element: T) {
    contents -= element
  }

  override def contains(element: T): Boolean = contents.contains(element)

  override def clear() {
    contents = Set()
  }

  override def iterator(): Iterator[T] = contents.iterator
}