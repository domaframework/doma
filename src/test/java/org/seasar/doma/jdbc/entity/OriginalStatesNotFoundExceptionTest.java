package org.seasar.doma.jdbc.entity;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class OriginalStatesNotFoundExceptionTest extends TestCase {

    public void test() throws Exception {
        OriginalStatesNotFoundException e = new OriginalStatesNotFoundException(new Exception(),
                "aaa", "bbb");
        System.out.println(e.getMessage());
        assertEquals("aaa", e.getEntityClassName());
        assertEquals("bbb", e.getFieldName());
    }
}
