package org.seasar.doma.it.domain;

import doma.domain.AbstractIntegerDomain;

public class IdDomain extends AbstractIntegerDomain<IdDomain> {

    public IdDomain() {
        super();
    }

    public IdDomain(Integer value) {
        super(value);
    }

}
