package org.seasar.doma.jdbc;

import org.seasar.doma.jdbc.SqlFileNotFoundException;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class SqlFileNotFoundExceptionTest extends TestCase {

    public void test() throws Exception {
        SqlFileNotFoundException e = new SqlFileNotFoundException("aaa");
        assertEquals("aaa", e.getPath());
        System.out.println(e.getMessage());
    }
}
