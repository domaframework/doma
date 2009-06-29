package org.seasar.doma.domain;

import org.seasar.doma.domain.DomainValueNullPointerException;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class DomainValueNullPointerExceptionTest extends TestCase {

    public void test() throws Exception {
        DomainValueNullPointerException e = new DomainValueNullPointerException();
        System.out.println(e.getMessage());
    }
}
