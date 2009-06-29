package org.seasar.doma.internal.expr.node;

import static org.seasar.doma.internal.util.Assertions.*;

/**
 * @author taedium
 * 
 */
public class NotOperatorNode implements LogicalOperatorNode {

    protected static final int PRIORITY = 30;

    protected final ExpressionLocation location;

    protected final String operator;

    protected ExpressionNode node;

    public NotOperatorNode(ExpressionLocation location, String operator) {
        assertNotNull(location, operator);
        this.location = location;
        this.operator = operator;
    }

    public ExpressionNode getNode() {
        return node;
    }

    public void setNode(ExpressionNode node) {
        this.node = node;
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
        return visitor.visitNotOperatorNode(this, p);
    }

    public ExpressionLocation getLocation() {
        return location;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return operator + node;
    }
}
