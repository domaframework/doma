package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Holder;

/**
 * @author taedium
 * 
 */
@Holder(valueType = Integer.class)
public class Ver {

    private final Integer value;

    public Ver(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
