package de.proskor.cft.model.simple
import de.proskor.cft.model.Port
import de.proskor.cft.model.Source

private class SimplePort(initialName: String) extends SimpleElement(initialName) with Port {
  private var kid: Option[Source] = None
  def input = kid

  def +=(input: Source) {
    require(input.isInstanceOf[SimpleElement])
    kid = Some(input)
  }

  def -=(input: Source) {
    require(input.isInstanceOf[SimpleElement])
    kid = None
  }
}