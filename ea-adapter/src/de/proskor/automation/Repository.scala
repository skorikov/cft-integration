package de.proskor.automation

import cli.EA.IRepository
import cli.EA.IPackage
import cli.EA.ICollection

object Repository {
  var peer: IRepository = null
  def models: Collection[Package] = new PackageCollection(peer.get_Models.asInstanceOf[ICollection])

  private[automation] def getPackageById(id: Int): IPackage = peer.GetPackageByID(id).asInstanceOf[IPackage]
}