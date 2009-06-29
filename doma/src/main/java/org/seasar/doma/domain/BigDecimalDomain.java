package org.seasar.doma.domain;

import java.math.BigDecimal;

/**
 * @author taedium
 * 
 */
public class BigDecimalDomain extends
        AbstractBigDecimalDomain<BigDecimalDomain> {

    public BigDecimalDomain() {
        super();
    }

    public BigDecimalDomain(BigDecimal value) {
        super(value);
    }

}
