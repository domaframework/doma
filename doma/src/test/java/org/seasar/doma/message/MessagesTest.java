package org.seasar.doma.message;

import org.seasar.doma.message.MessageCode;
import org.seasar.doma.message.Messages;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class MessagesTest extends TestCase {

    public void test() throws Exception {
        String message = Messages.getMessage(MessageCode.DOMA0001);
        assertNotNull(message);
    }
}
