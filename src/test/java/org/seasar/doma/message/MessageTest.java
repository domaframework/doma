package org.seasar.doma.message;

import java.util.Locale;
import junit.framework.TestCase;

public class MessageTest extends TestCase {

  private Locale locale;

  @Override
  protected void setUp() throws Exception {
    locale = Locale.getDefault();
    Locale.setDefault(Locale.US);
  }

  @Override
  protected void tearDown() throws Exception {
    Locale.setDefault(locale);
  }

  public void testDOMA0001() throws Exception {
    String message = Message.DOMA0001.getMessage("aaa", "bbb");
    assertNotNull(message);
    System.out.println(message);
  }

  public void testDOMA0001_ja() throws Exception {
    String message = Message.DOMA0001.getMessage("aaa", "bbb");
    Locale.setDefault(Locale.JAPAN);
    String message_jp = Message.DOMA0001.getMessage("aaa", "bbb");
    System.out.println(message);
    System.out.println(message_jp);
    assertFalse(message.equals(message_jp));
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
