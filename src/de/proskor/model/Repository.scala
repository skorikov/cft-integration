package de.proskor.model
import de.proskor.model.cft.Component

trait Repository extends Container {
  def selected: Option[Identity]
  def write(text: String)
  def query(sql: String): String
  def models: List[Model]
  def allPackages: List[Package] =  models.flatMap(_.allPackages)
  def allComponents: List[Component] = models.flatMap(_.allComponents)
  def show(diagram: Diagram)
  def get(id: Int): Element
}