package de.proskor.fel.impl

import java.util.{List => JavaList}
import scala.collection.JavaConversions.seqAsJavaList
import de.proskor.automation.Element
import de.proskor.automation.Package
import de.proskor.automation.Repository
import de.proskor.fel.container.EventTypeContainer
import de.proskor.fel.EventRepository
import de.proskor.fel.view.ViewRepository
import de.proskor.fel.view.View
import de.proskor.automation.Diagram

class EventRepositoryImpl2(repository: Repository) extends ViewRepository {
  private def fel: Package = {
    val model = repository.models.headOption getOrElse repository.models.add("Model", "Package")
    model.packages.find(_.name == "FEL") getOrElse model.packages.add("FEL", "Package")
  }

  override def getViews: JavaList[View] = allDiagrams(repository.models.head) map {
    case diagram: Diagram if diagram.stereotype == "CFT" => new ComponentFaultTreeImpl(diagram)
    case diagram: Diagram => new ArchitecturalViewImpl(diagram)
  }

  private def allDiagrams(pkg: Package): Seq[Diagram] =
    pkg.diagrams.toSeq ++ pkg.packages.flatMap(allDiagrams)

  private def allObjects(pkg: Package): Seq[Element] =
    pkg.elements.filter(_.stereotype == "EventTypeContainer").toSeq ++ pkg.packages.flatMap(allObjects)
}