package de.proskor.emc.test

import de.proskor.cft.model._
import java.io.InputStream
import java.util.Scanner
import org.eclipse.epsilon.eol.EolModule
import org.eclipse.epsilon.eol.IEolModule
import org.eclipse.epsilon.eol.models.IModel
import java.io.FileInputStream

abstract class AbstractTest {
  def main(args: Array[String]) {
    configure()
    run()
    finish()
  }

  private def run() = {
    val module: IEolModule = getModule(model, code)
    module.execute
  }

  def configure(): Unit
  def finish(): Unit
  def model: IModel
  def code: String

  protected def getModule(model: IModel, path: String): IEolModule = {
    val module: IEolModule = new EolModule;
    module.getContext.getModelRepository.addModel(model)
    val is: InputStream = new FileInputStream(path)
    module.parse(convertStreamToString(is))
    module
  }

  protected def convertStreamToString(is: InputStream): String = {
    new Scanner(is).useDelimiter("\\A").next
  }

  protected def formatAll[T <: Element](name: String, elements: Set[T]) =
    name + " { " + elements.map(format).mkString("; ") + " }"

  protected def formatTerm(name: String, elements: Set[Source], operator: String) =
    name + " = " + elements.map(_.name).mkString(" " + operator + " ")

  protected def format(element: Element): String = element match {
    case Event(name) => name
    case And(name, inputs) => formatTerm(name, inputs, "*")
    case Or(name, inputs) => formatTerm(name, inputs, "+")
    case Port(name, inputs) => formatTerm(name, inputs, "|")
    case Component(name, events, inports, outports, gates, components) =>
      formatAll(name, events ++ inports ++ outports ++ gates ++ components)
    case Package(name, packages, components) =>
      formatAll(name, packages ++ components)
    case Repository(name, packages) =>
      formatAll(name, packages)
  }
}