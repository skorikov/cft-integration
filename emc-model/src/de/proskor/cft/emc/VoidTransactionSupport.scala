package de.proskor.cft.emc
import org.eclipse.epsilon.eol.models.transactions.IModelTransactionSupport

object VoidTransactionSupport extends IModelTransactionSupport {
  override def startTransaction {}
  override def commitTransaction {}
  override def rollbackTransaction {}
}