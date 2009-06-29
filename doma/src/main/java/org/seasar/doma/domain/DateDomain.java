package org.seasar.doma.domain;

import java.sql.Date;

/**
 * @author taedium
 * 
 */
public class DateDomain extends AbstractDateDomain<DateDomain> {

    public DateDomain() {
        super();
    }

    public DateDomain(Date value) {
        super(value);
    }

}
