package org.seasar.doma.internal.expr.node;

import static org.seasar.doma.internal.util.AssertionUtil.*;

public class StaticMethodOperatorNode implements OperatorNode {

  protected static final int PRIORITY = 0;

  protected final ExpressionLocation location;

  protected final String expression;

  protected final String className;

  protected final String methodName;

  protected ExpressionNode parametersNode;

  @Override
  public int getPriority() {
    return PRIORITY;
  }

  public StaticMethodOperatorNode(
      ExpressionLocation location, String expression, String className, String methodName) {
    assertNotNull(location, className, methodName);
    this.location = location;
    this.expression = expression;
    this.className = className;
    this.methodName = methodName;
  }

  public String getClassName() {
    return className;
  }

  public String getMethodName() {
    return methodName;
  }

  public ExpressionNode getParametersNode() {
    return parametersNode;
  }

  public void setParametersNode(ExpressionNode parametersNode) {
    this.parametersNode = parametersNode;
  }

  @Override
  public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
    return visitor.visitStaticMethodOperatorNode(this, p);
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
    return expression + parametersNode;
  }
}
