package org.seasar.doma.it.domain;

import java.sql.Date;

import org.seasar.doma.domain.DateDomain;

public class HiredateDomain extends DateDomain<HiredateDomain> {

    private static final long serialVersionUID = 1L;

    public HiredateDomain() {
        super();
    }

    public HiredateDomain(Date value) {
        super(value);
    }

}
