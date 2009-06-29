package org.seasar.doma.internal.expr.node;

import static org.seasar.doma.internal.util.Assertions.*;

/**
 * @author taedium
 * 
 */
public abstract class AbstractArithmeticOperatorNode implements
        ArithmeticOperatorNode {

    protected final ExpressionLocation location;

    protected final String operator;

    protected final int priority;

    protected ExpressionNode leftNode;

    protected ExpressionNode rightNode;

    public AbstractArithmeticOperatorNode(ExpressionLocation location,
            String operator, int priority) {
        assertNotNull(location, operator);
        this.location = location;
        this.operator = operator;
        this.priority = priority;
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
        return priority;
    }

    @Override
    public String toString() {
        return leftNode + " " + operator + " " + rightNode;
    }

}
