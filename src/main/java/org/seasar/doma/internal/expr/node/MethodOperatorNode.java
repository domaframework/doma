package org.seasar.doma.internal.expr.node;

import static org.seasar.doma.internal.util.AssertionUtil.*;

public class MethodOperatorNode implements OperatorNode {

  protected static final int PRIORITY = 0;

  protected final ExpressionLocation location;

  protected final String expression;

  protected final String methodName;

  protected ExpressionNode targetObjectNode;

  protected ExpressionNode parametersNode;

  @Override
  public int getPriority() {
    return PRIORITY;
  }

  public MethodOperatorNode(ExpressionLocation location, String expression, String methodName) {
    assertNotNull(location, methodName);
    this.location = location;
    this.expression = expression;
    this.methodName = methodName;
  }

  public String getMethodName() {
    return methodName;
  }

  public ExpressionNode getTargetObjectNode() {
    return targetObjectNode;
  }

  public void setTargetObjectNode(ExpressionNode targetObjectNode) {
    this.targetObjectNode = targetObjectNode;
  }

  public ExpressionNode getParametersNode() {
    return parametersNode;
  }

  public void setParametersNode(ExpressionNode parametersNode) {
    this.parametersNode = parametersNode;
  }

  @Override
  public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
    return visitor.visitMethodOperatorNode(this, p);
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
    return targetObjectNode + expression + parametersNode;
  }
}
