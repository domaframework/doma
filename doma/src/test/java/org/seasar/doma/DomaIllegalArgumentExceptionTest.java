package org.seasar.doma;

import org.seasar.doma.DomaIllegalArgumentException;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class DomaIllegalArgumentExceptionTest extends TestCase {

    public void test() throws Exception {
        DomaIllegalArgumentException e = new DomaIllegalArgumentException(
                "aaa", null);
        assertEquals("aaa", e.getArgumentName());
        assertNull(e.getValue());
    }
}
