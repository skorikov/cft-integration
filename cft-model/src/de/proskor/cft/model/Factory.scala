package de.proskor.cft.model

trait Factory {
  def createRepository(name: String): Repository
  def createPackage(name: String): Package
  def createComponent(name: String): Component
  def createEvent(name: String): Event
  def createAnd(name: String): And
  def createOr(name: String): Or
  def createInport(name: String): Inport
  def createOutport(name: String): Outport
}

object Factory extends Factory {
  var default: Factory = simple.SimpleFactory

  override def createRepository(name: String): Repository = default.createRepository(name)
  override def createPackage(name: String): Package = default.createPackage(name)
  override def createComponent(name: String): Component = default.createComponent(name)
  override def createEvent(name: String): Event = default.createEvent(name)
  override def createAnd(name: String): And = default.createAnd(name)
  override def createOr(name: String): Or = default.createOr(name)
  override def createInport(name: String): Inport = default.createInport(name)
  override def createOutport(name: String): Outport = default.createOutport(name)

  def createPackage(parent: Package, name: String): Package = addTo(parent, default.createPackage(name))
  def createComponent(parent: Container, name: String): Component = addTo(parent, default.createComponent(name))
  def createEvent(parent: Component, name: String): Event = addTo(parent, default.createEvent(name))
  def createAnd(parent: Component, name: String): And = addTo(parent, default.createAnd(name))
  def createOr(parent: Component, name: String): Or = addTo(parent, default.createOr(name))
  def createInport(parent: Component, name: String): Inport = addTo(parent, default.createInport(name))
  def createOutport(parent: Component, name: String): Outport = addTo(parent, default.createOutport(name))

  private[this] def addTo[T <: Element](parent: Container, element: T): T = {
    parent += element
    element
  }
}