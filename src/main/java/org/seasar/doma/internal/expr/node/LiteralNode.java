package org.seasar.doma.internal.expr.node;

import static org.seasar.doma.internal.util.AssertionUtil.*;

/**
 * @author taedium
 * 
 */
public class LiteralNode implements ExpressionNode {

    protected final ExpressionLocation location;

    protected final String expression;

    protected final Object value;

    protected final Class<?> valueClass;

    public <T> LiteralNode(ExpressionLocation location, String expression, T value,
            Class<T> valueClass) {
        assertNotNull(location, expression, valueClass);
        this.location = location;
        this.expression = expression;
        this.value = value;
        this.valueClass = valueClass;
    }

    public Object getValue() {
        return value;
    }

    public Class<?> getValueClass() {
        return valueClass;
    }

    @Override
    public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
        return visitor.visitLiteralNode(this, p);
    }

    @Override
    public ExpressionLocation getLocation() {
        return location;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return expression;
    }
}
