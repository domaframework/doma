package org.seasar.doma.internal.expr.node;

import static org.seasar.doma.internal.util.Assertions.*;

/**
 * @author taedium
 * 
 */
public class OrOperatorNode implements LogicalOperatorNode {

    protected static final int PRIORITY = 10;

    protected final ExpressionLocation location;

    protected final String operator;

    protected ExpressionNode leftNode;

    protected ExpressionNode rightNode;

    public OrOperatorNode(ExpressionLocation location, String text) {
        assertNotNull(location, text);
        this.location = location;
        this.operator = text;
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
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
        return visitor.visitOrOperatorNode(this, p);
    }

    public ExpressionLocation getLocation() {
        return location;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", leftNode, operator, rightNode);
    }

}
