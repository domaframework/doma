package org.seasar.doma.internal.expr.node;

public interface ArithmeticOperatorNode extends OperatorNode {

  ExpressionNode getLeftNode();

  ExpressionNode getRightNode();
}
