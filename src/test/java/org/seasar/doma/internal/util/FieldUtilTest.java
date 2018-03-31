package org.seasar.doma.internal.util;

import junit.framework.TestCase;

public class FieldUtilTest extends TestCase {

  public String aaa;

  String bbb;

  public void testIsPublic() throws Exception {
    var aaa = FieldUtilTest.class.getField("aaa");
    assertTrue(FieldUtil.isPublic(aaa));

    var bbb = FieldUtilTest.class.getDeclaredField("bbb");
    assertFalse(FieldUtil.isPublic(bbb));
  }
}
