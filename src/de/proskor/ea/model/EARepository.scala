package de.proskor.ea.model
import de.proskor.model._
import EATraversable._
import javax.swing.JOptionPane

class EARepository(val repository: cli.EA.IRepository) extends Repository {
  val name = "/"
  def name_=(name: String) {}

  var outputInitialized = false

  val id: Long = 0

  val parent = None

  val kids = models

  def selected = {
    this write repository.GetContextItemType().toString + " is selected"
    repository.GetContextItemType.Value match {
    case cli.EA.ObjectType.otElement => Some(new EAElement(repository.GetContextObject().asInstanceOf[cli.EA.IElement], repository))
    case cli.EA.ObjectType.otPackage => Some(new EAPackage(repository.GetContextObject().asInstanceOf[cli.EA.IPackage], repository))
    case cli.EA.ObjectType.otDiagram => Some(new EADiagram(repository.GetContextObject().asInstanceOf[cli.EA.IDiagram], repository))
    case _ => None
  }}

  def write(text: String) {
    if (!outputInitialized) {
      repository.CreateOutputTab("CFT Integration")
      repository.EnsureOutputVisible("CFT Integration")
      outputInitialized = true
    }
    repository.WriteOutput("CFT Integration", text, 1)
  }

  val models = {
    val collection: Traversable[cli.EA.IPackage] = repository.get_Models.asInstanceOf[cli.EA.Collection]
    (for (model <- collection) yield new EAModel(model, repository)).toList
  }

  def query(sql: String) = repository.SQLQuery(sql)
}