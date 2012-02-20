package de.proskor.model

trait Container extends Entity {
  def kids: List[Entity]
  def allKids: List[Entity] = kids ::: kids.filter(_.isInstanceOf[Container]).flatMap(_.asInstanceOf[Container].allKids)
}