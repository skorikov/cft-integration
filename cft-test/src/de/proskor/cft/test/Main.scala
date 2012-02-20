package de.proskor.cft.test
import de.proskor.cft.model._

object Main {
  def main(args: Array[String]) {
    val repository = model
    println(format(repository))
  }

  private val model: Repository = {
    val repository = Repository("/")
    val pkg = Package(repository, "P1")
    val component = Component(pkg, "C1")
    val pkg2 = Package(repository, "P2")

    val e1 = Event(component, "a")
    val e2 = Event(component, "b")
    val e3 = Event(component, "c")
    val g1 = And(component, "g1")
    g1 += e1
    g1 += e2
    val g2 = Or(component, "g2")
    g2 += g1
    g2 += e3
    pkg2 += component
    repository
  }

  private def formatAll[T <: Element](name: String, elements: Set[T]) =
    name + " { " + elements.map(format).mkString("; ") + " }"

  private def formatTerm(name: String, elements: Set[Source], operator: String) =
    name + " = " + elements.map(_.name).mkString(" " + operator + " ")

  private def format(element: Element): String = element match {
    case Event(name) => name
    case And(name, inputs) => formatTerm(name, inputs, "*")
    case Or(name, inputs) => formatTerm(name, inputs, "+")
    case Component(name, events, inports, outports, gates, components) =>
      formatAll(name, events ++ inports ++ outports ++ gates ++ components)
    case Package(name, packages, components) =>
      formatAll(name, packages ++ components)
    case Repository(name, packages) =>
      formatAll(name, packages)
  }
}
