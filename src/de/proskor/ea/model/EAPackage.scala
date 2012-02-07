package de.proskor.ea.model
import EATraversable._
import de.proskor.ea.model.cft.EAComponent
import de.proskor.model._

class EAPackage(pkg: cli.EA.IPackage, repository: cli.EA.IRepository) extends Package {
  def name = pkg.get_Name.asInstanceOf[String]
  def name_=(name: String) {
    pkg.set_Name(name)
    pkg.Update()
  }

  val id: Long = pkg.get_PackageID

  def diagrams = {
    val collection: Traversable[cli.EA.IDiagram] = pkg.get_Diagrams.asInstanceOf[cli.EA.Collection]
    (for (diagram <- collection) yield new EADiagram(diagram, repository)).toList
  }

  def packages = {
    val collection: Traversable[cli.EA.IPackage] = pkg.get_Packages.asInstanceOf[cli.EA.Collection]
    (for (pkg <- collection) yield new EAPackage(pkg, repository)).toList
  }

  def allPackages = packages ::: packages.flatMap(_.allPackages)

  def elements = {
    val collection: Traversable[cli.EA.IElement] = pkg.get_Elements.asInstanceOf[cli.EA.Collection]
    (for (kid <- collection) yield new EAElement(kid, repository)).toList
  }

  def parent: Option[Container] = {
    val parentid = pkg.get_ParentID
    if (parentid > 0) {
      Some(new EAPackage(repository.GetPackageByID(parentid).asInstanceOf[cli.EA.IPackage], repository))
    } else {
      Some(new EARepository(repository))
    }
  }

  def kids: List[Entity] = packages ::: elements

  def components = elements.filter(_.stereotype == "Component").map(x => new EAComponent(x.asInstanceOf[EAElement].element, repository))

  def allComponents = allElements.filter(_.stereotype == "Component").map(x => new EAComponent(x.asInstanceOf[EAElement].element, repository))

  def createDiagram(name: String): Diagram = {
    val diagram = pkg.get_Diagrams.asInstanceOf[cli.EA.Collection].AddNew(name, "Logical").asInstanceOf[cli.EA.IDiagram]
    diagram.Update()
    new EADiagram(diagram, repository)
  }

  def addComponent = {
    val ne = pkg.get_Elements.asInstanceOf[cli.EA.Collection].AddNew("new", "Object").asInstanceOf[cli.EA.IElement]
    val component = new EAComponent(ne, repository)
    component.stereotype = "Component"
    component
  }
}