package org.seasar.doma.it.domain;

import doma.domain.AbstractStringDomain;

public class ValueDomain extends AbstractStringDomain<ValueDomain> {

    public ValueDomain() {
        super();
    }

    public ValueDomain(String value) {
        super(value);
    }

}
