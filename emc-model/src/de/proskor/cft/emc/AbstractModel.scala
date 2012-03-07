package de.proskor.cft.emc

import org.eclipse.epsilon.eol.models.IModel
import java.util.{List => JavaList}
import org.eclipse.epsilon.commons.util.StringProperties
import org.eclipse.epsilon.eol.models.transactions.IModelTransactionSupport
import collection.JavaConversions.seqAsJavaList

abstract class AbstractModel extends IModel {
  protected var name: String = ""
  private var aliases: Seq[String] = Seq[String]()
  private var readOnLoad: Boolean = false
  private var storeOnDisposal: Boolean = false

  override def getName: String = name
  override def setName(name: String) { this.name = name }
  override def getAliases: JavaList[String] = aliases

  override def isReadOnLoad: Boolean = readOnLoad
  override def setReadOnLoad(readOnLoad: Boolean) { this.readOnLoad = readOnLoad }
  override def isStoredOnDisposal: Boolean = storeOnDisposal
  override def setStoredOnDisposal(storedOnDisposal: Boolean) { this.storeOnDisposal = storeOnDisposal }

  override def load {}
  override def load(properties: StringProperties, basePath: String) {}
  override def store: Boolean = true
  override def store(location: String): Boolean = true
  override def dispose {}

  override def getTransactionSupport: IModelTransactionSupport = VoidTransactionSupport
}