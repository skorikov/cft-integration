package de.proskor.ea

trait TestRunner extends Writable {
  def test(clazz: Class[_]): Unit
}