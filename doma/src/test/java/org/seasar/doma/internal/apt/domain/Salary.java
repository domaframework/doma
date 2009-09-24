package org.seasar.doma.internal.apt.domain;

import java.math.BigDecimal;

import org.seasar.doma.Domain;

@Domain(valueType = BigDecimal.class)
public class Salary {

    private final BigDecimal value;

    public Salary(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

}
