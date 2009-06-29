package org.seasar.doma.internal.expr.node;

/**
 * @author taedium
 * 
 */
public class GeOperatorNode extends AbstractComparisonOperatorNode {

    public GeOperatorNode(ExpressionLocation location, String opCode) {
        super(location, opCode);
    }

    @Override
    public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
        return visitor.visitGeOperatorNode(this, p);
    }

}
