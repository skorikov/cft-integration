package de.proskor.ea.model
import de.proskor.model._
import EATraversable._

class EADiagram(val diagram: cli.EA.IDiagram, val repository: cli.EA.IRepository) extends Diagram {
  def name = diagram.get_Name.asInstanceOf[String]
  def name_=(name: String) {
    diagram.set_Name(name)
    diagram.Update()
  }

  val id: Int = diagram.get_DiagramID

  val nodes = {
    val collection: Traversable[cli.EA.IDiagramObject] = diagram.get_DiagramObjects().asInstanceOf[cli.EA.Collection]
    (for (obj <- collection) yield new EANode(obj, repository)).toList
  }

  def addNode = {
    val obj = diagram.get_DiagramObjects.asInstanceOf[cli.EA.Collection].AddNew("", "Object").asInstanceOf[cli.EA.IDiagramObject]
    val node = new EANode(obj, repository)
    diagram.get_DiagramObjects.asInstanceOf[cli.EA.Collection].Refresh()
    node
  }

  override def equals(that: Any) = that match {
    case other: EADiagram => diagram.get_DiagramGUID == other.diagram.get_DiagramGUID
    case _ => false
  }

  override val hashCode = diagram.get_DiagramGUID.hashCode
}