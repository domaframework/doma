package org.seasar.doma.domain;

import org.seasar.doma.domain.DomainUnsupportedOperationException;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class DomainUnsupportedOperationExceptionTest extends TestCase {

    public void test() throws Exception {
        DomainUnsupportedOperationException e = new DomainUnsupportedOperationException();
        System.out.println(e.getMessage());
    }

}
