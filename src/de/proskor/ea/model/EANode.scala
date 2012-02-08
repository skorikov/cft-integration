package de.proskor.ea.model

import cli.EA.IRepository
import de.proskor.model.Element
import de.proskor.model.Node
import de.proskor.ea.model.cft.EAOutput
import de.proskor.ea.model.cft.EAOr
import de.proskor.ea.model.cft.EAComponent
import de.proskor.ea.model.cft.EAEvent
import de.proskor.ea.model.cft.EAAnd
import de.proskor.ea.model.cft.EAInput

class EANode(val obj: cli.EA.IDiagramObject, val repository: cli.EA.IRepository) extends Node {
  def left = obj.get_left
  def left_=(left: Int) {
    val w = width
    obj.set_left(left)
    width = w
    obj.Update()
  }

  def top = -obj.get_top
  def top_=(top: Int) {
    val h = height
    obj.set_top(-top)
    height = h
    obj.Update()
  }

  def width = obj.get_right - obj.get_left
  def width_=(width: Int) {
    obj.set_right(obj.get_left + width)
    obj.Update()
  }

  def height = obj.get_top - obj.get_bottom
  def height_=(height: Int) {
    obj.set_bottom(obj.get_top - height)
    obj.Update()
  }

  protected def getElement(element: cli.EA.IElement): Element = element.get_Stereotype match {
    case "Event" =>  new EAEvent(element, repository)
    case "Input" => new EAInput(element, repository)
    case "Output" => new EAOutput(element, repository)
    case "OR" => new EAOr(element, repository)
    case "AND" => new EAAnd(element, repository)
    case "Component" => new EAComponent(element, repository)
    case _ => new EAElement(element, repository)
  }

  def element = getElement(repository.GetElementByID(obj.get_ElementID()).asInstanceOf[cli.EA.IElement])
  def element_=(element: Element) {
    obj.set_ElementID(element.id)
  }

  def sequence = obj.get_Sequence.asInstanceOf[Int]
  def sequence_=(sequence: Int) {
    obj.set_Sequence(sequence)
  }
}