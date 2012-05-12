package de.proskor.redundancy

import de.proskor.cft.model._

object RedundancyRemoval {
  def equivalent(first: Source, second: Source): Boolean = (first, second) match {
    case (Event(_), Event(_)) => first == second
    case (And(_, left), And(_, right)) => left.toSeq.permutations.exists(_.corresponds(right.toSeq)(equivalent))
    case (Or(_, left), Or(_, right)) => left.toSeq.permutations.exists(_.corresponds(right.toSeq)(equivalent))
    case (Port(_, left), Port(_, right)) => left.toSeq.permutations.exists(_.corresponds(right.toSeq)(equivalent))
    case _ => false
  }
}