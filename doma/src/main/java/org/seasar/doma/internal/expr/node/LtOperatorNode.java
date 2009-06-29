package org.seasar.doma.internal.expr.node;

/**
 * @author taedium
 * 
 */
public class LtOperatorNode extends AbstractComparisonOperatorNode {

    public LtOperatorNode(ExpressionLocation location, String opCode) {
        super(location, opCode);
    }

    @Override
    public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
        return visitor.visitLtOperatorNode(this, p);
    }

}
