package org.seasar.doma.it.entity;

import doma.Entity;
import doma.Id;
import doma.Version;
import doma.domain.BigDecimalDomain;
import doma.domain.IntegerDomain;
import doma.domain.StringDomain;
import doma.domain.TimestampDomain;

/**
 * 
 * @author taedium
 * 
 */
@Entity(listener = EmpListener.class)
public interface Emp {

    @Id
    IntegerDomain id();

    StringDomain name();

    BigDecimalDomain salary();

    @Version
    IntegerDomain version();

    TimestampDomain insertTimestamp();

    TimestampDomain updateTimestamp();

}
