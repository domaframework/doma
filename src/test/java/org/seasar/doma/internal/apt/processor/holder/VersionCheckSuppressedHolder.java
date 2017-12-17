package org.seasar.doma.internal.apt.processor.holder;

import java.math.BigDecimal;

import org.seasar.doma.Holder;

/**
 * @author taedium
 * 
 */
@Holder(valueType = BigDecimal.class, acceptNull = true)
public class VersionCheckSuppressedHolder {

    private final BigDecimal value;

    public VersionCheckSuppressedHolder(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
