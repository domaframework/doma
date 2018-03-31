package org.seasar.doma.internal.expr.node;

/** @author taedium */
public interface OperatorNode extends ExpressionNode {

  int getPriority();
}
