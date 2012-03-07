package de.proskor.cft.emc
import java.util.{List => JavaList, Collection => JavaCollection}
import de.proskor.cft.model._
import org.eclipse.epsilon.eol.models._
import org.eclipse.epsilon.eol.execute.introspection._
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException
import org.eclipse.epsilon.commons.util.StringProperties
import collection.JavaConversions._
import org.eclipse.epsilon.eol.exceptions.models.EolEnumerationValueNotFoundException
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException

class CftModel(var objects: Set[Element]) extends AbstractModel {
  private val allTypes = Set[String]("Element", "Container", "Repository", "Package", "Component", "Source", "Port", "Gate", "Event", "Inport", "Outport", "And", "Or")
  private val concreteTypes = Set[String]("Package", "Component", "Event", "Inport", "Outport", "And", "Or")

  override def getEnumerationValue(enumeration: String, label: String): AnyRef =
    throw new EolEnumerationValueNotFoundException(enumeration, label, getName)

  override def allContents: JavaCollection[AnyRef] = objects

  override def isModelElement(instance: AnyRef): Boolean = instance.isInstanceOf[Element]

  override def getAllOfType(typ: String): JavaCollection[AnyRef] = if (concreteTypes.contains(typ)) typ match {
    case "Repository" => objects.filter(_.isInstanceOf[Repository])
    case "Package" => objects.filter(element => element.isInstanceOf[Package]).filterNot(_.isInstanceOf[Repository])
    case "Component" => objects.filter(_.isInstanceOf[Component])
    case "Event" => objects.filter(_.isInstanceOf[Event])
    case "Inport" => objects.filter(_.isInstanceOf[Inport])
    case "Outport" => objects.filter(_.isInstanceOf[Outport])
    case "And" => objects.filter(_.isInstanceOf[And])
    case "Or" => objects.filter(_.isInstanceOf[Or])
  } else if (allTypes.contains(typ)) {
    Set[AnyRef]()
  } else {
    throw new EolModelElementTypeNotFoundException(name, typ)
  }

  override def getAllOfKind(typ: String): JavaCollection[AnyRef] = if (allTypes.contains(typ)) typ match {
    case "Element" => objects.filter(_.isInstanceOf[Element])
    case "Container" => objects.filter(_.isInstanceOf[Element])
    case "Port" => objects.filter(_.isInstanceOf[Port])
    case "Gate" => objects.filter(_.isInstanceOf[Gate])
    case "Source" => objects.filter(_.isInstanceOf[Source])
    case "Repository" => objects.filter(_.isInstanceOf[Repository])
    case "Package" => objects.filter(element => element.isInstanceOf[Package])
    case "Component" => objects.filter(_.isInstanceOf[Component])
    case "Event" => objects.filter(_.isInstanceOf[Event])
    case "Inport" => objects.filter(_.isInstanceOf[Inport])
    case "Outport" => objects.filter(_.isInstanceOf[Outport])
    case "And" => objects.filter(_.isInstanceOf[And])
    case "Or" => objects.filter(_.isInstanceOf[Or])
  } else {
    throw new EolModelElementTypeNotFoundException(name, typ)
  }

  override def getTypeOf(instance: Any): AnyRef = instance.getClass

  override def getTypeNameOf(instance: AnyRef): String = instance match {
    case _: Repository => "Repository"
    case _: Package => "Package"
    case _: Component => "Component"
    case _: Event => "Event"
    case _: Inport => "Inport"
    case _: Outport => "Outport"
    case _: And => "And"
    case _: Or => "Or"
    case _ => throw new IllegalArgumentException
  }

  override def getFullyQualifiedTypeNameOf(instance: AnyRef): String = getTypeNameOf(instance)

  override def createInstance(typ: String): AnyRef = throw new IllegalArgumentException

  override def createInstance(typ: String, parameters: JavaCollection[AnyRef]): AnyRef = {
    val (name, parent) = processParameters(parameters)
    typ match {
      case "Repository" => if (parent.isDefined) throw new IllegalArgumentException else create(Repository(name))
      case "Package" => if (parent.isDefined) create(Package(parent.get.asInstanceOf[Package], name)) else create(Package(name))
      case "Component" => if (parent.isDefined) create(Component(parent.get, name)) else create(Component(name))
      case "Event" => if (parent.isDefined) create(Event(parent.get.asInstanceOf[Component], name)) else create(Event(name))
      case "Inport" => if (parent.isDefined) create(Inport(parent.get.asInstanceOf[Component], name)) else create(Inport(name))
	  case "Outport" => if (parent.isDefined) create(Outport(parent.get.asInstanceOf[Component], name)) else create(Outport(name))
	  case "And" => if (parent.isDefined) create(And(parent.get.asInstanceOf[Component], name)) else create(And(name))
	  case "Or" => if (parent.isDefined) create(Or(parent.get.asInstanceOf[Component], name)) else create(Or(name))
      case _ => throw new EolModelElementTypeNotFoundException(getName, typ)
    }
  }

  override def deleteElement(instance: AnyRef) {
    if (instance.isInstanceOf[Element]) {
      instance.asInstanceOf[Element].parent.foreach {
        case container => container -= instance.asInstanceOf[Element]
      }
      objects -= instance.asInstanceOf[Element]
    } else {
      throw new EolRuntimeException
    }
  }

  private def create[T <: Element](element: T): T = {
    objects += element
    element
  }

  private def processParameters(parameters: JavaCollection[AnyRef]): (String, Option[Container]) = {
    parameters.size match {
      case 1 => (parameters.toSeq.get(0).asInstanceOf[String], None)
      case 2 => (parameters.toSeq.get(1).asInstanceOf[String], Some(parameters.toSeq.get(0).asInstanceOf[Container]))
      case _ => throw new IllegalArgumentException
    }
  }

  override def getElementById(id: String): AnyRef = null
  override def getElementId(instance: AnyRef): String = instance.hashCode.toString
  override def setElementId(instance: AnyRef, id: String) {}

  override def getPropertyGetter: IPropertyGetter = CftPropertyGetter
  override def getPropertySetter: IPropertySetter = CftPropertySetter

  override def hasType(typ: String): Boolean = allTypes.contains(typ)
  override def isInstantiable(typ: String): Boolean = concreteTypes.contains(typ)

  override def knowsAboutProperty(instance: Any, property: String): Boolean = instance match {
    case _: Package => Set("name", "parent", "elements", "packages", "components").contains(property)
    case _: Component => Set("name", "parent", "elements", "events", "gates", "inports", "outports", "components").contains(property)
    case _: Event => Set("name", "parent").contains(property)
    case _: Gate => Set("name", "parent", "input").contains(property)
    case _: Port => Set("name", "parent", "inputs").contains(property)
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
    case _: Component => typ == "Component"
    case _: Event => typ == "Event"
    case _: Inport => typ == "Inport"
    case _: Outport => typ == "Outport"
    case _: And => typ == "And"
    case _: Or => typ =="Or"
  } else {
    throw new EolModelElementTypeNotFoundException(name, typ)
  }

  override def isOfKind(instance: Any, typ: String): Boolean = if (allTypes.contains(typ)) instance match {
    case _: Repository => Set("Repository", "Package", "Container", "Element").contains(typ)
    case _: Package => Set("Package", "Container", "Element").contains(typ)
    case _: Component => Set("Component", "Container", "Element").contains(typ)
    case _: Event => Set("Event", "Source", "Element").contains(typ)
    case _: Inport => Set("Inport", "Source", "Port", "Element").contains(typ)
    case _: Outport => Set("Outport", "Source", "Port", "Element").contains(typ)
    case _: And => Set("And", "Source", "Gate", "Element").contains(typ)
    case _: Or => Set("Or", "Source", "Gate", "Element").contains(typ)
  } else {
    throw new EolModelElementTypeNotFoundException(name, typ)
  }
}