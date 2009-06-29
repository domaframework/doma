package org.seasar.doma.domain;

import java.sql.Time;

/**
 * @author taedium
 * 
 */
public class TimeDomain extends AbstractTimeDomain<TimeDomain> {

    public TimeDomain() {
        super();
    }

    public TimeDomain(Time value) {
        super(value);
    }

}
