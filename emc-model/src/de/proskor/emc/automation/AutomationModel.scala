package de.proskor.emc.automation

import java.util.{List => JavaList, Collection => JavaCollection}
import de.proskor.automation._
import de.proskor.emc._

import org.eclipse.epsilon.eol.models._
import org.eclipse.epsilon.eol.execute.introspection._
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException
import org.eclipse.epsilon.eol.exceptions.models.EolNotInstantiableModelElementTypeException
import org.eclipse.epsilon.commons.util.StringProperties
import collection.JavaConversions._
import org.eclipse.epsilon.eol.exceptions.models.EolEnumerationValueNotFoundException
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException

class AutomationModel(var objects: Set[AnyRef]) extends AbstractModel {
  private val allTypes = Set[String]("Collection", "Container", "Named", "Stereotyped", "Element", "Package", "Repository")
  private val concreteTypes = Set[String]("Package", "Element", "Repository")

  override def getEnumerationValue(enumeration: String, label: String): AnyRef =
    throw new EolEnumerationValueNotFoundException(enumeration, label, getName)

  override def allContents: JavaCollection[AnyRef] = objects

  override def isModelElement(instance: AnyRef): Boolean = instance.isInstanceOf[Element]

  override def getAllOfType(typ: String): JavaCollection[AnyRef] = if (concreteTypes.contains(typ)) typ match {
    case "Repository" => objects.filter(_.isInstanceOf[Repository])
    case "Package" => objects.filter(element => element.isInstanceOf[Package]).filterNot(_.isInstanceOf[Repository])
    case "Element" => objects.filter(_.isInstanceOf[Element])
  } else if (allTypes.contains(typ)) {
    Set[AnyRef]()
  } else {
    throw new EolModelElementTypeNotFoundException(name, typ)
  }

  override def getAllOfKind(typ: String): JavaCollection[AnyRef] = if (allTypes.contains(typ)) typ match {
    case "Element" => objects.filter(_.isInstanceOf[Element])
    case "Container" => objects.filter(_.isInstanceOf[Element])
    case "Repository" => objects.filter(_.isInstanceOf[Repository])
    case "Package" => objects.filter(element => element.isInstanceOf[Package])
  } else {
    throw new EolModelElementTypeNotFoundException(name, typ)
  }

  override def getTypeOf(instance: Any): AnyRef = instance.getClass

  override def getTypeNameOf(instance: AnyRef): String = instance match {
    case _: Repository => "Repository"
    case _: Package => "Package"
    case _: Element => "Element"
    case _ => throw new IllegalArgumentException
  }

  override def getFullyQualifiedTypeNameOf(instance: AnyRef): String = getTypeNameOf(instance)

  override def createInstance(typ: String): AnyRef = throw new IllegalArgumentException

  override def createInstance(typ: String, parameters: JavaCollection[AnyRef]): AnyRef = throw new IllegalArgumentException

  override def deleteElement(instance: AnyRef) = throw new EolRuntimeException

  override def getElementById(id: String): AnyRef = null
  override def getElementId(instance: AnyRef): String = instance.hashCode.toString
  override def setElementId(instance: AnyRef, id: String) {}

  override def getPropertyGetter: IPropertyGetter = AutomationPropertyGetter
  override def getPropertySetter: IPropertySetter = AutomationPropertySetter

  override def hasType(typ: String): Boolean = allTypes.contains(typ)
  override def isInstantiable(typ: String): Boolean = concreteTypes.contains(typ)

  override def knowsAboutProperty(instance: Any, property: String): Boolean = instance match {
    case _: Package => Set("name", "parent", "elements", "packages").contains(property)
    case _: Element => Set("name", "parent", "elements").contains(property)
    case _: Repository => Set("models").contains(property)
    case _: Collection[_] => Set("add").contains(property)
    case _ => false
  }

  override def owns(instance: AnyRef): Boolean = instance match {
    case element: Element => objects.contains(instance)
    case _ => false
  }

/*  private def contains(container: Container, element: Element): Boolean =
    container.elements.contains(element) ||
    container.elements.filter(_.isInstanceOf[Container]).asInstanceOf[Set[Container]].exists(contains(_, element))*/

  override def isOfType(instance: Any, typ: String): Boolean = if (allTypes.contains(typ)) instance match {
    case _: Repository => typ == "Repository"
    case _: Package => typ == "Package"
    case _: Element => typ == "Element"
  } else {
    throw new EolModelElementTypeNotFoundException(name, typ)
  }

  override def isOfKind(instance: Any, typ: String): Boolean = if (allTypes.contains(typ)) instance match {
    case _: Repository => Set("Repository", "Package", "Container", "Element").contains(typ)
    case _: Package => Set("Package", "Container", "Element").contains(typ)
    case _: Element => Set("Container", "Element").contains(typ)
  } else {
    throw new EolModelElementTypeNotFoundException(name, typ)
  }
}