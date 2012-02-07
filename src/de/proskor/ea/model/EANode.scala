package de.proskor.ea.model

import de.proskor.model.Node
import cli.EA.IRepository

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

  val element = new EAElement(repository.GetElementByID(obj.get_ElementID()).asInstanceOf[cli.EA.IElement], repository)
}