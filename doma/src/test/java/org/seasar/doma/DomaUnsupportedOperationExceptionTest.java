package org.seasar.doma;

import org.seasar.doma.DomaUnsupportedOperationException;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class DomaUnsupportedOperationExceptionTest extends TestCase {

    public void test() throws Exception {
        DomaUnsupportedOperationException e = new DomaUnsupportedOperationException(
                "aaa", "bbb");
        System.out.println(e.getMessage());
        assertEquals("aaa", e.getClassName());
        assertEquals("bbb", e.getMethodName());
    }
}
