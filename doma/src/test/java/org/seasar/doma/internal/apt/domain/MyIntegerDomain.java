package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.domain.IntegerDomain;

public class MyIntegerDomain extends IntegerDomain<MyIntegerDomain> {

    private static final long serialVersionUID = 1L;

    public MyIntegerDomain() {
        super();
    }

    public MyIntegerDomain(Integer value) {
        super(value);
    }

    public void foo() {
    }

    public <T> T bar() {
        return null;
    }

}
