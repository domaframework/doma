package org.seasar.doma;

import org.seasar.doma.DomaUnexpectedException;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class DomaUnexpectedExceptionTest extends TestCase {

    public void test() throws Exception {
        Exception exception = new Exception();
        DomaUnexpectedException e = new DomaUnexpectedException(exception);
        System.out.println(e.getMessage());
        assertSame(exception, e.getCause());
    }
}
