package de.proskor.automation

trait Collection[T] extends Iterable[T] {
  def add(name: String, typ: String): T
  def remove(element: T): Unit
  def contains(element: T): Boolean
  def clear(): Unit
}