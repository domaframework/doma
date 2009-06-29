package org.seasar.doma.internal.expr.node;

/**
 * @author taedium
 * 
 */
public class DivideOperatorNode extends AbstractArithmeticOperatorNode {

    protected static final int PRIORITY = 60;

    public DivideOperatorNode(ExpressionLocation location, String operator) {
        super(location, operator, PRIORITY);
    }

    @Override
    public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
        return visitor.visitDivideOperatorNode(this, p);
    }
}
