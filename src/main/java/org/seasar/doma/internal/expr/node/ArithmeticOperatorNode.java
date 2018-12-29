package org.seasar.doma.internal.expr.node;

/** @author taedium */
public interface ArithmeticOperatorNode extends OperatorNode {

  ExpressionNode getLeftNode();

  ExpressionNode getRightNode();
}
