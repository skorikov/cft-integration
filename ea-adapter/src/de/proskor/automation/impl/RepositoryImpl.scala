package de.proskor.automation.impl

import de.proskor.automation._
import de.proskor.automation.impl.collections._
import cli.EA.IRepository
import cli.EA.IPackage
import cli.EA.IDiagram
import cli.EA.IElement
import cli.EA.ICollection
import cli.EA.ObjectType

object RepositoryImpl extends Repository {
  var peer: IRepository = null
  var outputTab: String = "OUT"
  var outputInitialized: Boolean = false

  override def models: Collection[Package] = new PackageCollection(peer.get_Models.asInstanceOf[ICollection])

  override def context: Option[Identity] = peer.GetContextItemType.Value match {
    case ObjectType.otElement => Some(new ElementImpl(peer.GetContextObject.asInstanceOf[IElement]))
    case _ => None
  }

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

  def getPackageById(id: Int): IPackage = peer.GetPackageByID(id).asInstanceOf[IPackage]
  def getDiagramById(id: Int): IDiagram = peer.GetDiagramByID(id).asInstanceOf[IDiagram]
  def getElementById(id: Int): IElement = peer.GetElementByID(id).asInstanceOf[IElement]
}