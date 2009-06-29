package org.seasar.doma.internal.expr.node;

/**
 * @author taedium
 * 
 */
public class VariableNode implements ExpressionNode {

    protected final ExpressionLocation location;

    protected final String name;

    public VariableNode(ExpressionLocation location, String name) {
        this.location = location;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
        return visitor.visitVariableNode(this, p);
    }

    public ExpressionLocation getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return name;
    }

}