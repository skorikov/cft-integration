package de.proskor.cft.emc

import java.util.{Set => JavaSet}
import de.proskor.cft.model.Component
import de.proskor.cft.model.Element
import de.proskor.cft.model.Event
import collection.JavaConversions._
import org.eclipse.epsilon.eol.types.CollectionAnnotator
import org.eclipse.epsilon.eol.types.CollectionAnnotator.AnnotatedCollectionType

object CftPropertyGetter extends AbstractPropertyGetter {
  private val properties: Map[String, Set[String]] = Map[String, Set[String]](
    "Component" -> Set("name", "elements", "components"),
    "Event" -> Set("name", "parent")
  )

  override def invoke(instance: AnyRef, property: String): AnyRef = instance match {
    case component: Component => property match {
      case "name" => component.name
      case "elements" => toJavaSet(component.elements)
      case "components" => toJavaSet(component.components)
    }
    case event: Event => property match {
      case "name" => event.name
      case "parent" => event.parent.getOrElse(null)
      case _ => null
    }
  }

  override def hasProperty(instance: AnyRef, property: String): Boolean =
    properties.getOrElse(instance.getClass.getName, Set[String]()).contains(property)

  private def toJavaSet[T](elements: Set[T]): JavaSet[T] = {
    val result: JavaSet[T] = elements
    CollectionAnnotator.getInstance.annotate(result, AnnotatedCollectionType.Set);
    result
  }
}