package de.proskor.cft.model

abstract class CftFactory {
  def createRepository(name: String): Repository
  def createPackage(name: String): Package
  def createComponent(name: String): Component
  def createEvent(name: String): Event
  def createAnd(name: String): And
  def createOr(name: String): Or
  def createInport(name: String): Inport
  def createOutport(name: String): Outport

  def createPackage(parent: Package, name: String): Package = addTo(parent, createPackage(name))
  def createComponent(parent: Package, name: String): Component = addTo(parent, createComponent(name))
  def createEvent(parent: Component, name: String): Event = addTo(parent, createEvent(name))
  def createAnd(parent: Component, name: String): And = addTo(parent, createAnd(name))
  def createOr(parent: Component, name: String): Or = addTo(parent, createOr(name))
  def createInport(parent: Component, name: String): Inport = addTo(parent, createInport(name))
  def createOutport(parent: Component, name: String): Outport = addTo(parent, createOutport(name))

  private def addTo[T <: Element](parent: Container, element: T): T = {
    parent += element
    element
  }
}

object CftFactory {
  var default: CftFactory = simple.SimpleFactory
}