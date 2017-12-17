package org.seasar.doma.internal.expr.node;

/**
 * @author taedium
 * 
 */
public class VariableNode implements ExpressionNode {

    protected final ExpressionLocation location;

    protected final String expression;

    public VariableNode(ExpressionLocation location, String expression) {
        this.location = location;
        this.expression = expression;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    @Override
    public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
        return visitor.visitVariableNode(this, p);
    }

    @Override
    public ExpressionLocation getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return expression;
    }

}