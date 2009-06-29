package org.seasar.doma.domain;

import java.math.BigDecimal;

import org.seasar.doma.domain.BigDecimalDomain;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class BigDecimalDomainTest extends TestCase {

    public void testEquals() throws Exception {
        BigDecimalDomain domain = new BigDecimalDomain(new BigDecimal(10));
        BigDecimalDomain domain2 = new BigDecimalDomain(new BigDecimal(10));
        assertTrue(domain.equals(domain2));

        domain.setNull();
        assertFalse(domain.equals(domain2));

        domain2.setNull();
        assertTrue(domain.equals(domain2));

        domain.set(new BigDecimal(10));
        assertFalse(domain.equals(domain2));
    }

}
