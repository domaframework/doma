package org.seasar.doma.internal.expr.node;

public interface OperatorNode extends ExpressionNode {

  int getPriority();
}
