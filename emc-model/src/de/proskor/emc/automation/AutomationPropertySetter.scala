package de.proskor.emc.automation

import de.proskor.automation._
import de.proskor.emc._
import org.eclipse.epsilon.eol.exceptions.EolIllegalPropertyException

object AutomationPropertySetter extends AbstractPropertySetter {
  override def invoke(value: Any) = property match {
    case "name" => target match {
      case element: Named => element.name = value.asInstanceOf[String]
      case _ => throw new EolIllegalPropertyException(target, property, ast, context)
    }
    case "stereotype" => target match {
      case element: Stereotyped => element.stereotype = value.asInstanceOf[String]
      case _ => throw new EolIllegalPropertyException(target, property, ast, context)
    }
  }
}