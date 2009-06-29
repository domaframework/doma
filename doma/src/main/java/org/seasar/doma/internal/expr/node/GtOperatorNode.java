package org.seasar.doma.internal.expr.node;

/**
 * @author taedium
 * 
 */
public class GtOperatorNode extends AbstractComparisonOperatorNode {

    public GtOperatorNode(ExpressionLocation location, String opCode) {
        super(location, opCode);
    }

    @Override
    public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
        return visitor.visitGtOperatorNode(this, p);
    }

}
