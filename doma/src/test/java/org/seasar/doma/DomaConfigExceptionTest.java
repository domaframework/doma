package org.seasar.doma;

import org.seasar.doma.jdbc.DomaConfigException;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class DomaConfigExceptionTest extends TestCase {

    public void test() throws Exception {
        DomaConfigException e = new DomaConfigException("aaa", "bbb");
        System.out.println(e);
        assertEquals("aaa", e.getClassName());
        assertEquals("bbb", e.getMethodName());
    }
}
