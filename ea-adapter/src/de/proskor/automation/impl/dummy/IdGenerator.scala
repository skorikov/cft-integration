package de.proskor.automation.impl.dummy

private[dummy] object IdGenerator {
  private var id: Int = 0
  def next: Int = { id += 1; id }
}