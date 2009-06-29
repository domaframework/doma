package org.seasar.doma.jdbc;

import org.seasar.doma.jdbc.OptimisticLockException;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class OptimisticLockExceptionTest extends TestCase {

    public void test() throws Exception {
        OptimisticLockException e = new OptimisticLockException("aaa", "bbb");
        assertEquals("aaa", e.getRawSql());
        assertEquals("bbb", e.getFormattedSql());
        System.out.println(e.getMessage());
    }
}
