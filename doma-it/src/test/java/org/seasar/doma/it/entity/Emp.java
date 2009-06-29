package org.seasar.doma.it.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Version;
import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.domain.TimestampDomain;

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
