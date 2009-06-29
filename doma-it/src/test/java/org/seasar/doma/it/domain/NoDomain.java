package org.seasar.doma.it.domain;

import org.seasar.doma.domain.AbstractIntegerDomain;

public class NoDomain extends AbstractIntegerDomain<NoDomain> {

    public NoDomain() {
        super();
    }

    public NoDomain(Integer value) {
        super(value);
    }

}
