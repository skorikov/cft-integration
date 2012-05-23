package de.proskor.emc.test

import java.io.InputStream
import java.io.FileInputStream
import java.util.Scanner
import org.eclipse.epsilon.eol.models.IModel
import de.proskor.emc.cft.CftModel
import org.eclipse.epsilon.eol.EolModule
import org.eclipse.epsilon.eol.IEolModule
import de.proskor.cft.model._

object Main {
  def main(args: Array[String]) {
    configure()
    val repository = Repository("/")
    process(repository)
    println(format(repository))
  }

  private def configure() {
    de.proskor.automation.Repository.instance = de.proskor.automation.impl.dummy.DummyRepository
    Factory.use(de.proskor.cft.model.ea.EAFactory)
  }

  private def process(repository: Repository) = {
    val model: IModel = new CftModel(Set[Element](repository))
    model.setName("CFT")
    val module: IEolModule = getModule(model)
    module.execute
  }

  private def getModule(model: IModel): IEolModule = {
    val module: IEolModule = new EolModule();
    module.getContext.getModelRepository.addModel(model)
    val is: InputStream = new FileInputStream("/home/andrey/git/cft-integration/emc-model/epsilon/test.eol")
    module.parse(convertStreamToString(is))
    module
  }

  private def convertStreamToString(is: InputStream): String = {
    new Scanner(is).useDelimiter("\\A").next
  }

  private def formatAll[T <: Element](name: String, elements: Set[T]) =
    name + " { " + elements.map(format).mkString("; ") + " }"

  private def formatTerm(name: String, elements: Set[Source], operator: String) =
    name + " = " + elements.map(_.name).mkString(" " + operator + " ")

  private def format(element: Element): String = element match {
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
