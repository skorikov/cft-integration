package de.proskor.cft.emc

import org.eclipse.epsilon.eol.execute.introspection.IPropertySetter
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.commons.parse.AST;

abstract class AbstractPropertySetter extends IPropertySetter {
  private var ast: AST = null
  private var context: IEolContext = null
  private var objekt: AnyRef = null
  private var property: String = null

  override def getAst: AST = ast
  override def setAst(ast: AST) { this.ast = ast }

  override def getContext: IEolContext = context
  override def setContext(context: IEolContext) { this.context = context }

  override def getObject: AnyRef = objekt
  override def setObject(objekt: AnyRef) { this.objekt = objekt }

  override def getProperty: String = property
  override def setProperty(property: String) { this.property = property }
}