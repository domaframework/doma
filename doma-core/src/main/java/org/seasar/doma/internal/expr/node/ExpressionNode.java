package org.seasar.doma.internal.expr.node;

public interface ExpressionNode {

  ExpressionLocation getLocation();

  String getExpression();

  <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p);
}
