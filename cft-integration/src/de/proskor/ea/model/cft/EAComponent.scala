package de.proskor.ea.model.cft
import de.proskor.ea.model.EAElement
import de.proskor.model.cft.Component

class EAComponent(element: cli.EA.IElement, repository: cli.EA.IRepository) extends EAElement(element, repository) with Component {
  private def stereotypes(stereotype: String): List[cli.EA.IElement] = for (kid <- kids if kid.stereotype == stereotype)
    yield kid.asInstanceOf[EAElement].element

  private def select[T](stereotype: String)(f: cli.EA.IElement => T) = stereotypes(stereotype).map(f)

  def events = for (kid <- kids if kid.stereotype == "Event")
    yield new EAEvent(kid.asInstanceOf[EAElement].element, repository)

  def addEvent = {
    val ne = element.get_Elements.asInstanceOf[cli.EA.Collection].AddNew("", "Object").asInstanceOf[cli.EA.IElement]
    val event = new EAEvent(ne, repository)
    event.stereotype = "Event"
    element.get_Elements.asInstanceOf[cli.EA.Collection].Refresh()
    event
  }

  def components = for (kid <- kids if kid.stereotype == "Component")
    yield new EAComponent(kid.asInstanceOf[EAElement].element, repository)

  def addComponent = {
    val nc = element.get_Elements.asInstanceOf[cli.EA.Collection].AddNew("", "Object").asInstanceOf[cli.EA.IElement]
    val component = new EAComponent(nc, repository)
    component.stereotype = "Component"
    element.get_Elements.asInstanceOf[cli.EA.Collection].Refresh()
    component
  }

  def inputs = select("Input")(new EAInput(_, repository))

  def addInput = {
    val ni = element.get_Elements.asInstanceOf[cli.EA.Collection].AddNew("", "Port").asInstanceOf[cli.EA.IElement]
    val input = new EAInput(ni, repository)
    input.stereotype = "Input"
    element.get_Elements.asInstanceOf[cli.EA.Collection].Refresh()
    input
  }

  def outputs = select("Output")(new EAOutput(_, repository))

  def addOutput = {
    val no = element.get_Elements.asInstanceOf[cli.EA.Collection].AddNew("", "Port").asInstanceOf[cli.EA.IElement]
    val output = new EAOutput(no, repository)
    output.stereotype = "Output"
    element.get_Elements.asInstanceOf[cli.EA.Collection].Refresh()
    output
  }

  def gates = select("OR")(new EAOr(_, repository)) ::: select("AND")(new EAAnd(_, repository))

  def addOr = {
    val el = element.get_Elements.asInstanceOf[cli.EA.Collection].AddNew("", "Object").asInstanceOf[cli.EA.IElement]
    val gate = new EAOr(el, repository)
    gate.stereotype = "OR"
    element.get_Elements.asInstanceOf[cli.EA.Collection].Refresh()
    gate
  }

  def addAnd = {
    val el = element.get_Elements.asInstanceOf[cli.EA.Collection].AddNew("", "Object").asInstanceOf[cli.EA.IElement]
    val gate = new EAAnd(el, repository)
    gate.stereotype = "AND"
    element.get_Elements.asInstanceOf[cli.EA.Collection].Refresh()
    gate
  }
}