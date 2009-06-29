package org.seasar.doma.domain;

import org.seasar.doma.domain.DomainIncomparableException;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class DomainIncomparableExceptionTest extends TestCase {

    public void test() throws Exception {
        DomainIncomparableException e = new DomainIncomparableException();
        System.out.println(e.getMessage());
    }
}
