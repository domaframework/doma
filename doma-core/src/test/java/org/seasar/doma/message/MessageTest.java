package org.seasar.doma.message;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class MessageTest {

  @Test
  public void testDOMA0001() {
    String message = Message.DOMA0001.getMessage("aaa", "bbb");
    assertNotNull(message);
    System.out.println(message);
  }

  @Test
  public void testDOMA4019() {
    String message = Message.DOMA4019.getMessage("aaa", "bbb");
    assertNotNull(message);
    System.out.println(message);
  }

  @Test
  public void testDOMA4021() {
    String message = Message.DOMA4021.getMessage("aaa", "bbb");
    assertNotNull(message);
    System.out.println(message);
  }
}
