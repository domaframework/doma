package org.seasar.doma.internal.expr.node;

public interface ComparisonOperatorNode extends OperatorNode {

  ExpressionNode getLeftNode();

  ExpressionNode getRightNode();
}
