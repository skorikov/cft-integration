package de.proskor.emc.cft

import de.proskor.cft.model._
import de.proskor.emc._
import org.eclipse.epsilon.eol.exceptions.EolReadOnlyPropertyException

object CftPropertySetter extends AbstractPropertySetter {
  override def invoke(value: Any) = target match {
    case element: Element => property match {
      case "name" => element.name = value.asInstanceOf[String]
      case _ => throw new EolReadOnlyPropertyException
    }
  }
}