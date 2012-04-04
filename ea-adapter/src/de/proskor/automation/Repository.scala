package de.proskor.automation

import cli.EA.IRepository
import cli.EA.IPackage
import cli.EA.IDiagram
import cli.EA.IElement
import cli.EA.ICollection

object Repository {
  var peer: IRepository = null
  def models: Collection[Package] = new PackageCollection(peer.get_Models.asInstanceOf[ICollection])

  private[automation] def getPackageById(id: Int): IPackage = peer.GetPackageByID(id).asInstanceOf[IPackage]
  private[automation] def getDiagramById(id: Int): IDiagram = peer.GetDiagramByID(id).asInstanceOf[IDiagram]
  private[automation] def getElementById(id: Int): IElement = peer.GetElementByID(id).asInstanceOf[IElement]
}