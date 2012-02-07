package de.proskor.model.cft
import de.proskor.model.Element

trait Component extends Element {
  def events: List[Event]
  def addEvent: Event
  def components: List[Component]
  def addComponent: Component
  def inputs: List[Input]
  def addInput: Input
  def outputs: List[Output]
  def addOutput: Output
  def gates: List[Gate]
  def addOr: Gate
  def addAnd: Gate
  def matches(that: Component) = this.elementName == that.elementName

  override def toString = {
    "component " + name + " {" +
    indent(events map ("\n" + _ + ";") mkString) +
    indent(inputs map ("\n" + _) mkString) +
    indent(outputs map ("\n" + _) mkString) +
    indent(gates map ("\n" + _) mkString) +
    indent(components map ("\n" + _) mkString) +
    "\n}"
  }

  def indent(x: String) = "  " + x.replaceAll("\n", "\n  ")
}