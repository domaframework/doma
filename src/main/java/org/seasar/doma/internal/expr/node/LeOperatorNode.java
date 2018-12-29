package org.seasar.doma.internal.expr.node;

/** @author taedium */
public class LeOperatorNode extends AbstractComparisonOperatorNode {

  public LeOperatorNode(ExpressionLocation location, String opCode) {
    super(location, opCode);
  }

  @Override
  public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
    return visitor.visitLeOperatorNode(this, p);
  }
}
