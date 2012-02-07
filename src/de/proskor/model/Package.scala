package de.proskor.model
import cft._

trait Package extends Container {
  def diagrams: List[Diagram]
  def createDiagram(name: String): Diagram
  def packages: List[Package]
  def elements: List[Element]
  def allElements: List[Element] = (elements ::: (elements flatMap (_.allElements)) ::: (packages flatMap (_.allElements))).toSet.toList
  def components: List[Component]
  def allComponents: List[Component]
  def allPackages: List[Package]
  def addComponent: Component
}