package org.seasar.doma;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.message.MessageCode;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class DomaExceptionTest extends TestCase {

    public void testE0001() throws Exception {
        StringDomain domain = new StringDomain("aaa");
        try {
            domain.compareTo(null);
            fail();
        } catch (DomaIllegalArgumentException e) {
            System.out.println(e.getMessage());
            assertEquals(MessageCode.DOMA0001, e.getMessageCode());
        }
    }

    public void testE0002() throws Exception {
    }
}
