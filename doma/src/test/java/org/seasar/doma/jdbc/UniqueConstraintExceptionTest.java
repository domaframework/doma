package org.seasar.doma.jdbc;

import org.seasar.doma.jdbc.UniqueConstraintException;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class UniqueConstraintExceptionTest extends TestCase {

    public void test() throws Exception {
        UniqueConstraintException e = new UniqueConstraintException("aaa",
                "bbb", new Exception());
        assertEquals("aaa", e.getRawSql());
        assertEquals("bbb", e.getFormattedSql());
        System.out.println(e.getMessage());
    }
}
