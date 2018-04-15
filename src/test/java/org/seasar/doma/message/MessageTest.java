package org.seasar.doma.message;

import java.util.Locale;
import java.util.TreeSet;
import java.util.regex.Pattern;
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
    var message = Message.DOMA0001.getMessage("aaa", "bbb");
    assertNotNull(message);
    System.out.println(message);
  }

  public void testDOMA0001_ja() throws Exception {
    var message = Message.DOMA0001.getMessage("aaa", "bbb");
    Locale.setDefault(Locale.JAPAN);
    var message_jp = Message.DOMA0001.getMessage("aaa", "bbb");
    System.out.println(message);
    System.out.println(message_jp);
    assertFalse(message.equals(message_jp));
  }

  public void testDOMA4019() throws Exception {
    var message = Message.DOMA4019.getMessage("aaa", "bbb");
    assertNotNull(message);
    System.out.println(message);
  }

  public void testDOMA4021() throws Exception {
    var message = Message.DOMA4021.getMessage("aaa", "bbb");
    assertNotNull(message);
    System.out.println(message);
  }

  /**
   * Test that the same code is used for English setting and Japanese setting,
   * also test that the same parameters are used.
   *
   * @throws Exception
   */
  public void testCompareEnAndJa() throws Exception {

    var p = Pattern.compile("\\{(\\d+)\\}");
    var enArray = Message.class.getEnumConstants();
    var jaArray = Message_ja.class.getEnumConstants();
    assertEquals(enArray.length, jaArray.length);

    for (int i = 0; i < enArray.length; i++) {
      var en = enArray[i];
      var ja = jaArray[i];
      assertEquals(en.getCode(), ja.getCode());
      var enSet = new TreeSet<String>();
      var enMatcher = p.matcher(en.getMessagePattern());
      while (enMatcher.find()) {
        enSet.add(enMatcher.group(1));
      }
      var jaSet = new TreeSet<String>();
      var jaMatcher = p.matcher(ja.getMessagePattern());
      while (jaMatcher.find()) {
        jaSet.add(jaMatcher.group(1));
      }
      System.out.println(en.getCode() + enSet + " - " + ja.getCode() + jaSet);
      assertEquals(enSet, jaSet);
    }
  }
}
