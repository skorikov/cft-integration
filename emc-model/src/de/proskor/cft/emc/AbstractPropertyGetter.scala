package de.proskor.cft.emc

import org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.commons.parse.AST;

abstract class AbstractPropertyGetter extends IPropertyGetter {
  private var ast: AST = null
  private var context: IEolContext = null

  override def getAst: AST = ast
  override def setAst(ast: AST) { this.ast = ast }

  override def getContext: IEolContext = context
  override def setContext(context: IEolContext) { this.context = context }
}