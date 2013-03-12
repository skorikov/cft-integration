package de.proskor.cft.model.simple

import de.proskor.cft.model.{Component, Source, Target}

private abstract class SimpleTarget(initialName: String) extends SimpleElement(initialName) with Target {
  private var kids: Set[Source] = Set()
  
  def inputs: Set[Source] = kids

  override def parent: Option[Component] = container.asInstanceOf[Option[Component]]

  def add(input: Source) {
    require(input.isInstanceOf[SimpleElement])
    kids += input
  }

  def remove(input: Source) {
    require(input.isInstanceOf[SimpleElement] && kids.contains(input))
    kids -= input
  }
}