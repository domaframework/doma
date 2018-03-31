package org.seasar.doma.internal.expr.node;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

/** @author taedium */
public abstract class AbstractArithmeticOperatorNode implements ArithmeticOperatorNode {

  protected final ExpressionLocation location;

  protected final String expression;

  protected final int priority;

  protected ExpressionNode leftNode;

  protected ExpressionNode rightNode;

  protected AbstractArithmeticOperatorNode(
      ExpressionLocation location, String expression, int priority) {
    assertNotNull(location, expression);
    this.location = location;
    this.expression = expression;
    this.priority = priority;
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
    return priority;
  }

  @Override
  public String toString() {
    return leftNode + " " + expression + " " + rightNode;
  }
}
