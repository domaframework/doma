package org.seasar.doma.internal.expr.node;

public interface LogicalBinaryOperatorNode extends LogicalOperatorNode {

    ExpressionNode getLeftNode();

    ExpressionNode getRightNode();
}
