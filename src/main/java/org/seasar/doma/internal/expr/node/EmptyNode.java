package org.seasar.doma.internal.expr.node;

/**
 * @author taedium
 * 
 */
public class EmptyNode implements ExpressionNode {

    protected final ExpressionLocation location;

    public EmptyNode(ExpressionLocation location) {
        this.location = location;
    }

    @Override
    public String getExpression() {
        return "";
    }

    @Override
    public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
        return visitor.visitEmptyNode(this, p);
    }

    @Override
    public ExpressionLocation getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "";
    }

}
