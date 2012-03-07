package de.proskor.cft.emc

import de.proskor.cft.model.Component
import de.proskor.cft.model.Event

object CftPropertySetter extends AbstractPropertySetter {
  override def invoke(value: Any) = getObject match {
    case component: Component => 
    case event: Event => getProperty match {
      case "name" => event.name = value.asInstanceOf[String]
    }
  }
}