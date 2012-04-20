package de.proskor.cft.emc

import java.util.{Set => JavaSet}
import de.proskor.cft.model._
import collection.JavaConversions._
import org.eclipse.epsilon.eol.types.CollectionAnnotator
import org.eclipse.epsilon.eol.types.CollectionAnnotator.AnnotatedCollectionType

object CftPropertyGetter extends AbstractPropertyGetter {
  override def invoke(instance: AnyRef, property: String): AnyRef = instance match {
    case pkg: Package => property match {
      case "name" => pkg.name
      case "parent" => pkg.parent.getOrElse(null)
      case "elements" => toJavaSet(pkg.elements)
      case "packages" => toJavaSet(pkg.packages)
      case "components" => toJavaSet(pkg.components)
    }
    case component: Component => property match {
      case "name" => component.name
      case "parent" => component.parent.getOrElse(null)
      case "elements" => toJavaSet(component.elements)
      case "events" => toJavaSet(component.events)
      case "inports" => toJavaSet(component.inports)
      case "outports" => toJavaSet(component.outports)
      case "gates" => toJavaSet(component.gates)
      case "components" => toJavaSet(component.components)
    }
    case target: Target => property match {
      case "name" => target.name
      case "parent" => target.parent.getOrElse(null)
      case "inputs" => toJavaSet(target.inputs)
    }
    case event: Event => property match {
      case "name" => event.name
      case "parent" => event.parent.getOrElse(null)
    }
  }

  override def hasProperty(instance: AnyRef, property: String): Boolean = false

  private def toJavaSet[T](elements: Set[T]): JavaSet[T] = {
    val result: JavaSet[T] = elements
    CollectionAnnotator.getInstance.annotate(result, AnnotatedCollectionType.Set);
    result
  }
}