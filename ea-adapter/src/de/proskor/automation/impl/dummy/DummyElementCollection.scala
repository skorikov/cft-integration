package de.proskor.automation.impl.dummy

import de.proskor.automation.{Collection, Element, Package}

class DummyElementCollection(parent: Element)
extends DummyCollection[Element, Element](parent, (name: String, typ: String, parent: Element) => new DummyElement(parent, null, 0, name)) {
/*  private var contents: Map[Int, Element] = Map()

  override def add(name: String, typ: String): Element = {
    val id = IdGenerator.next
    val element = new DummyElement(parent, pkg, id, name)
    contents += (id -> element)
    element
  }

  override def delete(element: Element) = element match {
    case el: DummyElement => contents -= el.id
  }

  override def contains(element: Element): Boolean = element match {
    case el: DummyElement => contents.keySet.contains(el.id)
  }

  override def clear() {
    contents = Map()
  }

  override def iterator: Iterator[Element] = new Iterator[Element] {
    val keys: Seq[Int] = contents.keys.toSeq
    var current: Int = -1
    override def next: Element = {
      current += 1
      contents(keys(current))
    }
    override def hasNext: Boolean = current + 1 < contents.size
  }*/
}