package org.seasar.doma.message;

import junit.framework.TestCase;

public class MessageTest extends TestCase {

  public void testDOMA0001() throws Exception {
    String message = Message.DOMA0001.getMessage("aaa", "bbb");
    assertNotNull(message);
    System.out.println(message);
  }

  public void testDOMA4019() throws Exception {
    String message = Message.DOMA4019.getMessage("aaa", "bbb");
    assertNotNull(message);
    System.out.println(message);
  }

  public void testDOMA4021() throws Exception {
    String message = Message.DOMA4021.getMessage("aaa", "bbb");
    assertNotNull(message);
    System.out.println(message);
  }
}
