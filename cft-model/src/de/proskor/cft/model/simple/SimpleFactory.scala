package de.proskor.cft.model.simple
import de.proskor.cft.model._

object SimpleFactory extends Factory {
  override def createRepository(name: String): Repository = new SimpleRepository(name)
  override def createPackage(name: String): Package = new SimplePackage(name)
  override def createComponent(name: String): Component = new SimpleComponent(name)
  override def createEvent(name: String): Event = new SimpleEvent(name)
  override def createAnd(name: String): And = new SimpleAnd(name)
  override def createOr(name: String): Or = new SimpleOr(name)
  override def createInport(name: String): Inport = new SimpleInport(name)
  override def createOutport(name: String): Outport = new SimpleOutport(name)
}