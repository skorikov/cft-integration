package de.proskor.cft.emc
import org.eclipse.epsilon.eol.execute.operations.contributors.IOperationContributorProvider
import org.eclipse.epsilon.eol.execute.operations.contributors.OperationContributor

trait CftOperationContributorProvider extends IOperationContributorProvider {
  override def getOperationContributor: OperationContributor = null
}