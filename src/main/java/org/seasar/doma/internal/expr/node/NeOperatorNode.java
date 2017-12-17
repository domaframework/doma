package org.seasar.doma.internal.expr.node;

/**
 * @author taedium
 * 
 */
public class NeOperatorNode extends AbstractComparisonOperatorNode {

    public NeOperatorNode(ExpressionLocation location, String opCode) {
        super(location, opCode);
    }

    @Override
    public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
        return visitor.visitNeOperatorNode(this, p);
    }

}
