package org.seasar.doma.internal.expr.node;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

/** @author taedium */
public class NewOperatorNode implements OperatorNode {

  protected static final int PRIORITY = 0;

  protected final ExpressionLocation location;

  protected final String expression;

  protected final String className;

  protected ExpressionNode parametersNode;

  public NewOperatorNode(ExpressionLocation location, String expression, String className) {
    assertNotNull(location, expression, className);
    this.location = location;
    this.expression = expression;
    this.className = className;
  }

  public String getClassName() {
    return className;
  }

  public ExpressionNode getParametersNode() {
    return parametersNode;
  }

  public void setParametersNode(ExpressionNode parametersNode) {
    this.parametersNode = parametersNode;
  }

  @Override
  public int getPriority() {
    return PRIORITY;
  }

  @Override
  public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
    return visitor.visitNewOperatorNode(this, p);
  }

  @Override
  public ExpressionLocation getLocation() {
    return location;
  }

  @Override
  public String getExpression() {
    return expression;
  }

  @Override
  public String toString() {
    return expression + " " + className + parametersNode;
  }
}
