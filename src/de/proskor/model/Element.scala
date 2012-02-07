package de.proskor.model

trait Element extends Container {
  val typ: String
  def elements: List[Element]
  def allElements: List[Element] = (elements ::: (elements flatMap (_.allElements))).toSet.toList
  val classifier: Option[Element]
  var stereotype: String
  var stereotypes: List[String]
}