package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.Domain;

@Domain(valueType = int.class)
public class PrimitiveValueDomain {

    private final int value;

    public PrimitiveValueDomain(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
