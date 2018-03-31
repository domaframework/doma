package org.seasar.doma.internal.expr.node;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

/** @author taedium */
public class ParensNode implements ExpressionNode {

  protected final ExpressionLocation location;

  protected final ExpressionNode node;

  public ParensNode(ExpressionLocation location, ExpressionNode node) {
    assertNotNull(location);
    this.location = location;
    this.node = node;
  }

  public ExpressionNode getNode() {
    return node;
  }

  @Override
  public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
    return visitor.visitParensNode(this, p);
  }

  @Override
  public ExpressionLocation getLocation() {
    return location;
  }

  @Override
  public String getExpression() {
    return "(";
  }

  @Override
  public String toString() {
    return "(" + node + ")";
  }
}
