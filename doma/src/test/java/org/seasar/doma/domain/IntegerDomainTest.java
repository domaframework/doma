package org.seasar.doma.domain;

import org.seasar.doma.domain.IntegerDomain;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class IntegerDomainTest extends TestCase {

    public void testEquals() throws Exception {
        IntegerDomain domain = new IntegerDomain(1);
        IntegerDomain domain2 = new IntegerDomain(1);
        assertTrue(domain.equals(domain2));

        domain.setNull();
        assertFalse(domain.equals(domain2));

        domain2.setNull();
        assertTrue(domain.equals(domain2));

        domain.set(1);
        assertFalse(domain.equals(domain2));

    }
}
