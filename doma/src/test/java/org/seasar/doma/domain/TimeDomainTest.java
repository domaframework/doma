package org.seasar.doma.domain;

import java.sql.Time;

import org.seasar.doma.domain.TimeDomain;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class TimeDomainTest extends TestCase {

    public void testEquals() throws Exception {
        TimeDomain domain = new TimeDomain(Time.valueOf("16:31:10"));
        TimeDomain domain2 = new TimeDomain(Time.valueOf("16:31:10"));
        assertTrue(domain.equals(domain2));

        domain.setNull();
        assertFalse(domain.equals(domain2));

        domain2.setNull();
        assertTrue(domain.equals(domain2));

        domain.set(Time.valueOf("16:31:10"));
        assertFalse(domain.equals(domain2));
    }
}
