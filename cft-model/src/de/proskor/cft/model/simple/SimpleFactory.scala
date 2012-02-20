package de.proskor.cft.model.simple
import de.proskor.cft.model._

object SimpleFactory extends CftFactory {
  def createRepository(name: String): Repository = new SimpleRepository(name)
  def createPackage(name: String): Package = new SimplePackage(name)
  def createComponent(name: String): Component = new SimpleComponent(name)
  def createEvent(name: String): Event = new SimpleEvent(name)
  def createAnd(name: String): And = new SimpleAnd(name)
  def createOr(name: String): Or = new SimpleOr(name)
  def createInport(name: String): Inport = new SimpleInport(name)
  def createOutport(name: String): Outport = new SimpleOutport(name)
}