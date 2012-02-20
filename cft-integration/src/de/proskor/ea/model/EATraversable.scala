package de.proskor.ea.model

class EATraversable[T](val collection: cli.EA.Collection) extends Traversable[T] {
  def foreach[U](f: T => U) = {
    val enumerator = collection.GetEnumerator()
    while (enumerator.MoveNext()) {
      f(enumerator.get_Current().asInstanceOf[T])
    }
  }
}

object EATraversable {
  implicit def coll2trav[T](collection: cli.EA.Collection): Traversable[T] = new EATraversable(collection)
}