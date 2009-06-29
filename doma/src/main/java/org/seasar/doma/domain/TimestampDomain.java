package org.seasar.doma.domain;

import java.sql.Timestamp;

/**
 * @author taedium
 * 
 */
public class TimestampDomain extends AbstractTimestampDomain<TimestampDomain> {

    public TimestampDomain() {
        super();
    }

    public TimestampDomain(Timestamp value) {
        super(value);
    }

}
