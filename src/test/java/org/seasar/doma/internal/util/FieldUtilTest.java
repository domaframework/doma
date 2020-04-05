package org.seasar.doma.internal.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;

import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;

public class FieldUtilTest {

  public String aaa;

  String bbb;

  @Test
  public void testIsPublic() throws Exception {
    Field aaa = FieldUtilTest.class.getField("aaa");
    assertTrue(FieldUtil.isPublic(aaa));

    Field bbb = FieldUtilTest.class.getDeclaredField("bbb");
    assertFalse(FieldUtil.isPublic(bbb));
  }
}
