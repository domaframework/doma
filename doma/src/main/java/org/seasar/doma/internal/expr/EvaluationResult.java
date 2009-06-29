package org.seasar.doma.internal.expr;

import static org.seasar.doma.internal.util.Assertions.*;

/**
 * @author taedium
 * 
 */
public class EvaluationResult {

    protected final Object value;

    protected final Class<?> valueClass;

    public EvaluationResult(Object value, Class<?> valueClass) {
        assertNotNull(valueClass);
        this.value = value;
        this.valueClass = valueClass;
    }

    public Class<?> getValueClass() {
        return valueClass;
    }

    public Object getValue() {
        return value;
    }

    public boolean getBooleanValue() {
        if (Boolean.class.isInstance(value)) {
            return Boolean.class.cast(value).booleanValue();
        }
        return false;
    }

    @Override
    public String toString() {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

}
