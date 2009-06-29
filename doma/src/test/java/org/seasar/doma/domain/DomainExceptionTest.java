package org.seasar.doma.domain;

import org.seasar.doma.domain.DomainIncomparableException;
import org.seasar.doma.domain.DomainValueNullPointerException;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.message.MessageCode;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class DomainExceptionTest extends TestCase {

    public void testE1001() throws Exception {
        StringDomain domain = new StringDomain("aaa");
        try {
            domain.lt(new StringDomain());
            fail();
        } catch (DomainIncomparableException e) {
            System.out.println(e.getMessage());
            assertEquals(MessageCode.DOMA1001, e.getMessageCode());
        }
    }

    public void testE1002() throws Exception {
        StringDomain domain = new StringDomain();
        try {
            domain.isEmpty();
            fail();
        } catch (DomainValueNullPointerException e) {
            System.out.println(e.getMessage());
            assertEquals(MessageCode.DOMA1002, e.getMessageCode());
        }
    }

}
