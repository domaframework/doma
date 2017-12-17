package org.seasar.doma.internal.expr.node;

import static org.seasar.doma.internal.util.AssertionUtil.*;

/**
 * @author taedium
 * 
 */
public class NotOperatorNode implements LogicalOperatorNode {

    protected static final int PRIORITY = 30;

    protected final ExpressionLocation location;

    protected final String expression;

    protected ExpressionNode node;

    public NotOperatorNode(ExpressionLocation location, String expression) {
        assertNotNull(location, expression);
        this.location = location;
        this.expression = expression;
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

    @Override
    public ExpressionLocation getLocation() {
        return location;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return expression + node;
    }
}
