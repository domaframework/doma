package org.seasar.doma.internal.expr.node;

/**
 * @author taedium
 * 
 */
public interface ExpressionNode {

    ExpressionLocation getLocation();

    <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p);
}
