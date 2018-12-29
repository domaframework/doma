package org.seasar.doma.internal.util;

import java.lang.reflect.Field;
import junit.framework.TestCase;

public class FieldUtilTest extends TestCase {

  public String aaa;

  String bbb;

  public void testIsPublic() throws Exception {
    Field aaa = FieldUtilTest.class.getField("aaa");
    assertTrue(FieldUtil.isPublic(aaa));

    Field bbb = FieldUtilTest.class.getDeclaredField("bbb");
    assertFalse(FieldUtil.isPublic(bbb));
  }
}
