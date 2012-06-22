package de.proskor.emc.automation

import java.util.{Set => JavaSet}
import de.proskor.automation._
import de.proskor.emc._
import collection.JavaConversions._
import org.eclipse.epsilon.eol.types.CollectionAnnotator
import org.eclipse.epsilon.eol.types.CollectionAnnotator.AnnotatedCollectionType

object AutomationPropertyGetter extends AbstractPropertyGetter {
  override def invoke(instance: AnyRef, property: String): AnyRef = instance match {
    case element: Element => property match {
    //  case "id" => element.id
      case "name" => element.name
      case "stereotype" => element.stereotype
      case "elements" => toJavaSet(element.elements.toSet)
      case "parent" => element.parent.getOrElse(null)
      case "pkg" => element.pkg
      case "connectors" => element.connectors
    }
    case pkg: Package => property match {
    //  case "id" => pkg.id
      case "name" => pkg.name
      case "elements" => toJavaSet(pkg.elements.toSet)
      case "parent" => pkg.parent.getOrElse(null)
      case "packages" => toJavaSet(pkg.packages.toSet)
      case "diagrams" => pkg.diagrams
    }
    case repository: Repository => property match {
      case "models" => repository.models
    }
  }

  override def hasProperty(instance: AnyRef, property: String): Boolean = false

  private def toJavaSet[T](elements: Set[T]): JavaSet[T] = {
    val result: JavaSet[T] = elements
    CollectionAnnotator.getInstance.annotate(result, AnnotatedCollectionType.Set);
    result
  }
}