package org.seasar.doma.internal.expr.node;

import static org.seasar.doma.internal.util.AssertionUtil.*;

public abstract class AbstractComparisonOperatorNode implements ComparisonOperatorNode {

  protected static final int PRIORITY = 40;

  protected final ExpressionLocation location;

  protected final String expression;

  protected ExpressionNode leftNode;

  protected ExpressionNode rightNode;

  protected AbstractComparisonOperatorNode(ExpressionLocation location, String expression) {
    assertNotNull(location, expression);
    this.location = location;
    this.expression = expression;
  }

  @Override
  public ExpressionNode getLeftNode() {
    return leftNode;
  }

  public void setLeftNode(ExpressionNode leftNode) {
    this.leftNode = leftNode;
  }

  @Override
  public ExpressionNode getRightNode() {
    return rightNode;
  }

  public void setRightNode(ExpressionNode rightNode) {
    this.rightNode = rightNode;
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
  public int getPriority() {
    return PRIORITY;
  }

  @Override
  public String toString() {
    return leftNode + " " + expression + " " + rightNode;
  }
}
