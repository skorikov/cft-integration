package de.proskor.ea

trait Extension {
  def start(): Unit
  def stop(): Unit
  def test(): Unit
  def runTests(): Unit
  def write(text: String): Unit
}