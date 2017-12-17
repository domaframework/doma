package org.seasar.doma.jdbc;

import junit.framework.TestCase;

import org.seasar.doma.jdbc.entity.NamingType;

/**
 * @author nakamura-to
 *
 */
public class NamingTest extends TestCase {

    public void testNONE() throws Exception {
        Naming naming = Naming.NONE;

        assertEquals("hogeFoo", naming.apply(null, "hogeFoo"));
        assertEquals("hogeFoo", naming.revert(null, "hogeFoo"));

        assertEquals("HOGE_FOO", naming.apply(NamingType.SNAKE_UPPER_CASE, "hogeFoo"));
        assertEquals("hogeFoo", naming.revert(NamingType.SNAKE_UPPER_CASE, "HOGE_FOO"));
    }

    public void testSNAKE_UPPER_CASE() throws Exception {
        Naming naming = Naming.SNAKE_UPPER_CASE;

        assertEquals("HOGE_FOO", naming.apply(null, "hogeFoo"));
        assertEquals("hogeFoo", naming.revert(null, "HOGE_FOO"));

        assertEquals("hoge_foo", naming.apply(NamingType.SNAKE_LOWER_CASE, "hogeFoo"));
        assertEquals("hogeFoo", naming.revert(NamingType.SNAKE_LOWER_CASE, "hoge_foo"));
    }

}
