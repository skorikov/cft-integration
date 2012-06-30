package de.proskor.automation

trait Writable {
  def write(text: String): Unit
  def clear(): Unit
}