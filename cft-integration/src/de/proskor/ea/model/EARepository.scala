package de.proskor.ea.model
import de.proskor.model._
import EATraversable._
import javax.swing.JOptionPane
import de.proskor.ea.model.cft.EAOutput
import de.proskor.ea.model.cft.EAOr
import de.proskor.ea.model.cft.EAEvent
import de.proskor.ea.model.cft.EAAnd
import de.proskor.ea.model.cft.EAInput
import de.proskor.ea.model.cft.EAComponent

class EARepository(val repository: cli.EA.IRepository) extends Repository {
  val name = "/"
  def name_=(name: String) {}

  var outputInitialized = false

  val id: Int = 0

  val parent = None

  def kids = models

  def selected = {
    repository.GetContextItemType.Value match {
    case cli.EA.ObjectType.otElement => Some(getElement(repository.GetContextObject().asInstanceOf[cli.EA.IElement]))
    case cli.EA.ObjectType.otPackage => Some(new EAPackage(repository.GetContextObject().asInstanceOf[cli.EA.IPackage], repository))
    case cli.EA.ObjectType.otDiagram => Some(new EADiagram(repository.GetContextObject().asInstanceOf[cli.EA.IDiagram], repository))
    case _ => None
  }}

  def get(id: Int) = getElement(repository.GetElementByID(id).asInstanceOf[cli.EA.IElement])

  protected def getElement(element: cli.EA.IElement): Element = element.get_Stereotype match {
    case "Event" =>  new EAEvent(element, repository)
    case "Input" => new EAInput(element, repository)
    case "Output" => new EAOutput(element, repository)
    case "OR" => new EAOr(element, repository)
    case "AND" => new EAAnd(element, repository)
    case "Component" => new EAComponent(element, repository)
    case _ => new EAElement(element, repository)
  }

  def write(text: String) {
    if (!outputInitialized) {
      repository.CreateOutputTab("CFT Integration")
      repository.EnsureOutputVisible("CFT Integration")
      outputInitialized = true
    }
    repository.WriteOutput("CFT Integration", text, 1)
  }

  def models = {
    val collection: Traversable[cli.EA.IPackage] = repository.get_Models.asInstanceOf[cli.EA.Collection]
    (for (model <- collection) yield new EAModel(model, repository)).toList
  }

  def show(diagram: Diagram) {
    repository.OpenDiagram(diagram.id)
  }

  def query(sql: String) = repository.SQLQuery(sql)
}