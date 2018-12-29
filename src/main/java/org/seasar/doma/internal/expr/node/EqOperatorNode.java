package org.seasar.doma.internal.expr.node;

public class EqOperatorNode extends AbstractComparisonOperatorNode {

  public EqOperatorNode(ExpressionLocation location, String opCode) {
    super(location, opCode);
  }

  @Override
  public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
    return visitor.visitEqOperatorNode(this, p);
  }
}
