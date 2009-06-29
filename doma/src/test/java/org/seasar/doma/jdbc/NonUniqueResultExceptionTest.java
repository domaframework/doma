package org.seasar.doma.jdbc;

import org.seasar.doma.jdbc.NonUniqueResultException;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class NonUniqueResultExceptionTest extends TestCase {

    public void test() throws Exception {
        NonUniqueResultException e = new NonUniqueResultException("aaa", "bbb");
        assertEquals("aaa", e.getRawSql());
        assertEquals("bbb", e.getFormattedSql());
        System.out.println(e.getMessage());
    }
}
