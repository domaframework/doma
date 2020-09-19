package org.seasar.doma.internal.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import org.junit.jupiter.api.Test;

public class ConstructorUtilTest {

  @Test
  public void testToSignature() throws Exception {
    Constructor<String> constructor =
        String.class.getConstructor(char[].class, int.class, int.class);
    assertEquals(
        "java.lang.String(char[], int, int)", ConstructorUtil.createSignature(constructor));
  }

  @Test
  public void testGetConstructor() throws Exception {
    Constructor<String> constructor =
        String.class.getConstructor(char[].class, int.class, int.class);
    String result =
        ConstructorUtil.newInstance(constructor, new char[] {'a', 'b', 'c', 'd', 'e'}, 1, 3);
    assertEquals("bcd", result);
  }

  public static class Hoge {
    public Hoge(String name) {}
  }
}
