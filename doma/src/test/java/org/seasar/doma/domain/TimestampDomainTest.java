package org.seasar.doma.domain;

import java.sql.Timestamp;

import org.seasar.doma.domain.TimestampDomain;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class TimestampDomainTest extends TestCase {

    public void testEquals() throws Exception {
        TimestampDomain domain = new TimestampDomain(Timestamp
                .valueOf("2009-05-08 16:31:10"));
        TimestampDomain domain2 = new TimestampDomain(Timestamp
                .valueOf("2009-05-08 16:31:10"));
        assertTrue(domain.equals(domain2));

        domain.setNull();
        assertFalse(domain.equals(domain2));

        domain2.setNull();
        assertTrue(domain.equals(domain2));

        domain.set(Timestamp.valueOf("2009-05-08 16:31:10"));
        assertFalse(domain.equals(domain2));
    }
}
