package de.proskor.cft.merge
import de.proskor.cft.model.Element

class MergeTrace {
  var conflicts = Set[(Element, Element)]()
  private var left = Map[Element, Element]()
  private var right = Map[Element, Element]()
  private var trace = Map[Element, (Option[Element], Option[Element])]()

  def map(left: Element, right: Element, target: Element) {
    mapLeft(left, target)
    mapRight(right, target)
  }

  def mapLeft(source: Element, target: Element) {
    left += source -> target
    trace.get(target) match {
      case None => trace += (target -> (Some(source), None))
      case Some((None, Some(right))) => trace += (target -> (Some(source), Some(right)))
      case Some((None, None)) => throw new IllegalStateException
    }
  }

  def mapRight(source: Element, target: Element) {
    right += source -> target
    trace.get(target) match {
      case None => trace += (target -> (None, Some(source)))
      case Some((Some(left), None)) => trace += (target -> (Some(left), Some(source)))
      case Some((None, None)) => throw new IllegalStateException
    }
  }

  def conflict(left: Element, right: Element) {
    conflicts += ((left, right))
  }

  def target(element: Element): Element = left.orElse(right)(element)
  def sources(element: Element): (Option[Element], Option[Element]) = trace(element)
}