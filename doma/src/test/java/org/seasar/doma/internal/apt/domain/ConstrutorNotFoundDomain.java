package org.seasar.doma.internal.apt.domain;

import java.math.BigDecimal;

import org.seasar.doma.Domain;

@Domain(valueType = BigDecimal.class)
public class ConstrutorNotFoundDomain {

    private BigDecimal value;

    public BigDecimal getValue() {
        return value;
    }
}
