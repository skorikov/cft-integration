package de.proskor.automation.impl

import cli.EA.ICollection
import cli.EA.IDiagram
import cli.EA.IElement
import cli.EA.IPackage
import cli.EA.IRepository
import cli.EA.ObjectType
import de.proskor.automation.impl.collections._
import de.proskor.automation._

object RepositoryImpl extends Repository {
  var peer: IRepository = null
  var outputTab: String = "OUT"
  var outputInitialized: Boolean = false

  override def models: Collection[Package] = new PackageCollection(peer.get_Models.asInstanceOf[ICollection])

  override def context: Option[Identity] = peer.GetContextItemType.Value match {
    case ObjectType.otElement => Some(new ElementImpl(peer.GetContextObject.asInstanceOf[IElement]))
    case _ => None
  }

  override def diagram: Option[Diagram] =
    Option(peer.GetCurrentDiagram.asInstanceOf[IDiagram]).map(new DiagramImpl(_))

  private def withInitializedOutput(block: => Unit) {
    if (!outputInitialized) {
      peer.CreateOutputTab(outputTab)
      outputInitialized = true;
    }
    peer.EnsureOutputVisible(outputTab)
    block
  }

  override def write(text: String) = withInitializedOutput {
    peer.WriteOutput(outputTab, text, 0)
  }

  override def clear() = withInitializedOutput {
    peer.ClearOutput(outputTab)
  }

  def getPackageById(id: Int): IPackage = peer.GetPackageByID(id).asInstanceOf[IPackage]
  def getDiagramById(id: Int): IDiagram = peer.GetDiagramByID(id).asInstanceOf[IDiagram]
  def getElementById(id: Int): IElement = peer.GetElementByID(id).asInstanceOf[IElement]
}