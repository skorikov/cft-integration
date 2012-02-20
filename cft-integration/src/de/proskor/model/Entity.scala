package de.proskor.model

trait Entity extends Named with Identity {
  def parent: Option[Container]
  def fullName: String = parent match {
    case Some(entity) => entity.fullName + "/" + name
    case None => name
  }
  def elementName: String = parent match {
    case None => name
    case Some(pkg: Package) => name
    case Some(entity: Entity) => entity.elementName + "/" + name
    case _ => name
  }
}