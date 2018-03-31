package org.seasar.doma.internal.expr.node;

public class AddOperatorNode extends AbstractArithmeticOperatorNode {

  protected static final int PRIORITY = 50;

  public AddOperatorNode(ExpressionLocation location, String operator) {
    super(location, operator, PRIORITY);
  }

  @Override
  public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
    return visitor.visitAddOperatorNode(this, p);
  }
}
