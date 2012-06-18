package de.proskor.ea

trait TestRunner {
  def test(clazz: Class[_]): Unit
}