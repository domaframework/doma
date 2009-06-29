package org.seasar.doma.internal.expr.node;

import static org.seasar.doma.internal.util.Assertions.*;

/**
 * @author taedium
 * 
 */
public class LiteralNode implements ExpressionNode {

    protected final ExpressionLocation location;

    protected final String text;

    protected final Object value;

    protected final Class<?> valueClass;

    public <T> LiteralNode(ExpressionLocation location, String text, T value,
            Class<T> valueClass) {
        assertNotNull(location, text, valueClass);
        this.location = location;
        this.text = text;
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

    public ExpressionLocation getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return text;
    }
}
