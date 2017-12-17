package org.seasar.doma.internal.apt.processor.holder;

import java.math.BigDecimal;

import org.seasar.doma.Holder;

@Holder(valueType = Override.class)
public class UnsupportedValueTypeHolder {

    private final BigDecimal value;

    public UnsupportedValueTypeHolder(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
