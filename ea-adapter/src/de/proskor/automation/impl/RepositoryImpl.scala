package de.proskor.automation.impl

import de.proskor.automation._
import de.proskor.automation.impl.collections._
import cli.EA.IRepository
import cli.EA.IPackage
import cli.EA.IDiagram
import cli.EA.IElement
import cli.EA.ICollection

object RepositoryImpl extends Repository {
  override def models: Collection[Package] = new PackageCollection(peer.get_Models.asInstanceOf[ICollection])

  var peer: IRepository = null
  def getPackageById(id: Int): IPackage = peer.GetPackageByID(id).asInstanceOf[IPackage]
  def getDiagramById(id: Int): IDiagram = peer.GetDiagramByID(id).asInstanceOf[IDiagram]
  def getElementById(id: Int): IElement = peer.GetElementByID(id).asInstanceOf[IElement]
}