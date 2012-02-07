package de.proskor.ea.model
import de.proskor.model._
import EATraversable._

class EAElement(val element: cli.EA.IElement, val repository: cli.EA.IRepository) extends Element {
  def name = element.get_Name.asInstanceOf[String]
  def name_=(name: String) {
    element.set_Name(name)
    element.Update()
  }

  val id: Long = element.get_ElementID

  val typ = element.get_Type.asInstanceOf[String]

  def elements = {
    val collection: Traversable[cli.EA.IElement] = element.get_Elements.asInstanceOf[cli.EA.Collection]
    (for (kid <- collection) yield new EAElement(kid, repository)).toList
  }

  def kids = elements

  val classifier = {
    val id = element.get_ClassifierID()
    if (id > 0) {
      val clazz = repository.GetElementByID(id).asInstanceOf[cli.EA.IElement]
      if (clazz != null) Some(new EAElement(clazz, repository))
      else None
    } else None
  }

  def stereotypes = element.get_StereotypeEx.asInstanceOf[String].split(",").toList
  def stereotypes_=(stereotypes: List[String]) {
    element.set_StereotypeEx(stereotypes mkString ",")
    element.Update()
  }

  def stereotype = element.get_Stereotype.asInstanceOf[String]
  def stereotype_=(stereotype: String) {
    element.set_Stereotype(stereotype)
    element.Update()
  }

  def parent: Option[Container] = {
    if (element.get_ParentID > 0) {
      Some(new EAElement(repository.GetElementByID(element.get_ParentID).asInstanceOf[cli.EA.IElement], repository))
    } else {
      Some(new EAPackage(repository.GetPackageByID(element.get_PackageID).asInstanceOf[cli.EA.IPackage], repository))
    }
  }

  override def equals(that: Any) = that match {
    case other: EAElement => element.get_ElementGUID == other.element.get_ElementGUID
    case _ => false
  }

  override val hashCode = element.get_ElementGUID.hashCode
}