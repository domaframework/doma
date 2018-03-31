package org.seasar.doma.internal.util;

import junit.framework.TestCase;

/** @author taedium */
public class MethodUtilTest extends TestCase {

  public void testCreateSignature() {
    String signature =
        MethodUtil.createSignature(
            "hoge", new Class<?>[] {String.class, int.class, Integer[].class});
    assertEquals("hoge(java.lang.String, int, java.lang.Integer[])", signature);
  }
}
