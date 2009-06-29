package org.seasar.doma.internal.expr.node;

/**
 * @author taedium
 * 
 */
public interface ComparisonOperatorNode extends OperatorNode {

    ExpressionNode getLeftNode();

    ExpressionNode getRightNode();
}
