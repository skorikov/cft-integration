package de.proskor.model
import fel.FailureMode

trait Element extends Container {
  val typ: String
  def elements: List[Element]
  def allElements: List[Element] = (elements ::: (elements flatMap (_.allElements))).toSet.toList
  val classifier: Option[Element]
  var stereotype: String
  var stereotypes: List[String]
  var failureModes: Seq[FailureMode]
  def allFailureModes: Seq[FailureMode] = failureModes ++ ancestors.flatMap(_.allFailureModes)
  def ancestors: Seq[Element]
  def allAncestors: Seq[Element] = ancestors ++ ancestors.flatMap(_.allAncestors)
}