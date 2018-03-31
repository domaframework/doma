package org.seasar.doma.internal.expr.node;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

public class OrOperatorNode implements LogicalBinaryOperatorNode {

  protected static final int PRIORITY = 10;

  protected final ExpressionLocation location;

  protected final String expression;

  protected ExpressionNode leftNode;

  protected ExpressionNode rightNode;

  public OrOperatorNode(ExpressionLocation location, String expression) {
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
  public int getPriority() {
    return PRIORITY;
  }

  @Override
  public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
    return visitor.visitOrOperatorNode(this, p);
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
    return String.format("%s %s %s", leftNode, expression, rightNode);
  }
}
