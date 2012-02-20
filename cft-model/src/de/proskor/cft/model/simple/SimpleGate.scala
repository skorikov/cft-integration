package de.proskor.cft.model.simple
import de.proskor.cft.model.Gate
import de.proskor.cft.model.Source

private abstract class SimpleGate(initialName: String) extends SimpleElement(initialName) with Gate {
  private var kids = Set[Source]()
  def inputs = kids

  def +=(input: Source) {
    require(input.isInstanceOf[SimpleElement])
    kids += input
  }

  def -=(input: Source) {
    require(input.isInstanceOf[SimpleElement] && kids.contains(input))
    kids -= input
  }
}