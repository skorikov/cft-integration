package de.proskor.cft.emc

import org.eclipse.epsilon.eol.execute.introspection.IPropertySetter
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.commons.parse.AST;

abstract class AbstractPropertySetter extends IPropertySetter {
  protected var ast: AST = null
  protected var context: IEolContext = null
  protected var target: AnyRef = null
  protected var property: String = null

  override def getAst: AST = ast
  override def setAst(ast: AST) { this.ast = ast }

  override def getContext: IEolContext = context
  override def setContext(context: IEolContext) { this.context = context }

  override def getObject: AnyRef = target
  override def setObject(target: AnyRef) { this.target = target }

  override def getProperty: String = property
  override def setProperty(property: String) { this.property = property }
}