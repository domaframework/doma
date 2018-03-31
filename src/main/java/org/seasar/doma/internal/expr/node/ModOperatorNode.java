package org.seasar.doma.internal.expr.node;

/** @author taedium */
public class ModOperatorNode extends AbstractArithmeticOperatorNode {

  protected static final int PRIORITY = 60;

  public ModOperatorNode(ExpressionLocation location, String operator) {
    super(location, operator, PRIORITY);
  }

  @Override
  public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
    return visitor.visitModOperatorNode(this, p);
  }
}
