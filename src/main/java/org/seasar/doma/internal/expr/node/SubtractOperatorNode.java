package org.seasar.doma.internal.expr.node;

/**
 * @author taedium
 * 
 */
public class SubtractOperatorNode extends AbstractArithmeticOperatorNode {

    protected static final int PRIORITY = 50;

    public SubtractOperatorNode(ExpressionLocation location, String operator) {
        super(location, operator, PRIORITY);
    }

    @Override
    public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
        return visitor.visitSubtractOperatorNode(this, p);
    }
}
