package org.seasar.doma.internal.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MethodUtilTest {

  @Test
  public void testCreateSignature() {
    String signature =
        MethodUtil.createSignature(
            "hoge", new Class<?>[] {String.class, int.class, Integer[].class});
    assertEquals("hoge(java.lang.String, int, java.lang.Integer[])", signature);
  }
}
