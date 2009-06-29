package org.seasar.doma.domain;

import java.sql.Date;

import org.seasar.doma.domain.DateDomain;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class DateDomainTest extends TestCase {

    public void testEquals() throws Exception {
        DateDomain domain = new DateDomain(Date.valueOf("2009-5-8"));
        DateDomain domain2 = new DateDomain(Date.valueOf("2009-5-8"));
        assertTrue(domain.equals(domain2));

        domain.setNull();
        assertFalse(domain.equals(domain2));

        domain2.setNull();
        assertTrue(domain.equals(domain2));

        domain.set(Date.valueOf("2009-5-8"));
        assertFalse(domain.equals(domain2));
    }
}
