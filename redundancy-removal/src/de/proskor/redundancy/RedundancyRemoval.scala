package de.proskor.redundancy

import de.proskor.cft.model._

object RedundancyRemoval {
  def equivalent(first: Source, second: Source): Boolean = (first, second) match {
    case (Event(_), Event(_)) => first == second
    case (And(_, left), And(_, right)) => equivalent(left, right)
    case (Or(_, left), Or(_, right)) => equivalent(left, right)
    case (Port(_, left), Port(_, right)) => equivalent(left, right)
    case _ => false
  }

  def equivalent(first: Set[Source], second: Set[Source]): Boolean = equivalent(first.toList, second.toList)

  def equivalent(first: List[Source], second: List[Source]): Boolean = first match {
    case Nil => second.isEmpty
    case x :: xs => second.find(equivalent(x, _)).exists(m => equivalent(xs, second.filterNot(_ == m)))
  }
}