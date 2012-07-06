package de.proskor.fel.impl

import de.proskor.automation.impl.DiagramImpl
import de.proskor.automation.Diagram
import de.proskor.automation.Package
import de.proskor.automation.Repository
import de.proskor.fel.view.ArchitecturalView
import de.proskor.fel.view.ComponentFaultTree

class ComponentFaultTreeImpl(diagram: Diagram) extends ViewImpl(diagram) with ComponentFaultTree {
  private def fel: Package = {
    val repository = Repository.instance
    val model = repository.models.headOption getOrElse repository.models.add("Model", "Package")
    model.packages.find(_.name == "FEL") getOrElse model.packages.add("FEL", "Package")
  }
  
  override def getContext: ArchitecturalView = {
    val list = for {
      tv <- fel.element.taggedValues if tv.name == "context"
      (cft, context) = decode(tv.value)
      if cft == diagram
    } yield new ArchitecturalViewImpl(context)
    list.headOption orNull
  }

  private def decode(text: String): (Diagram, Diagram) = {
    val parts = text.split(":")
    val left = parts(0).toInt
    val right = parts(1).toInt
    val diagrams = allDiagrams(Repository.instance.models.head)
    (diagrams.find(_.id == left).get, diagrams.find(_.id == right).get) 
  }

  private def allDiagrams(pkg: Package): Seq[Diagram] =
    pkg.diagrams.toSeq ++ pkg.packages.flatMap(allDiagrams)

  override def setContext(context: ArchitecturalView) {
    val tv = fel.element.taggedValues.add("context", "TaggedValue")
    tv.value = diagram.id.toString + ":" + context.asInstanceOf[ArchitecturalViewImpl].diagram.id.toString
  }
}