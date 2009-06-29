package org.seasar.doma.internal.expr.node;

import static org.seasar.doma.internal.util.Assertions.*;

/**
 * @author taedium
 * 
 */
public abstract class AbstractComparisonOperatorNode implements
        ComparisonOperatorNode {

    protected static final int PRIORITY = 40;

    protected final ExpressionLocation location;

    protected final String operator;

    protected ExpressionNode leftNode;

    protected ExpressionNode rightNode;

    public AbstractComparisonOperatorNode(ExpressionLocation location,
            String operator) {
        assertNotNull(location, operator);
        this.location = location;
        this.operator = operator;
    }

    public ExpressionNode getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(ExpressionNode leftNode) {
        this.leftNode = leftNode;
    }

    public ExpressionNode getRightNode() {
        return rightNode;
    }

    public void setRightNode(ExpressionNode rightNode) {
        this.rightNode = rightNode;
    }

    @Override
    public ExpressionLocation getLocation() {
        return location;
    }

    @Override
    public String getOperator() {
        return operator;
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public String toString() {
        return leftNode + " " + operator + " " + rightNode;
    }

}
