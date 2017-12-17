package org.seasar.doma.internal.apt.processor.holder;

import java.math.BigDecimal;

import org.seasar.doma.Holder;

@Holder(valueType = BigDecimal.class)
public class ConstrutorNotFoundHolder {

    private BigDecimal value;

    public BigDecimal getValue() {
        return value;
    }
}
