package org.seasar.doma.internal.util;

import java.lang.reflect.Constructor;
import junit.framework.TestCase;

public class ConstructorUtilTest extends TestCase {

  public void testToSignature() throws Exception {
    Constructor<String> constructor =
        String.class.getConstructor(char[].class, int.class, int.class);
    assertEquals(
        "java.lang.String(char[], int, int)", ConstructorUtil.createSignature(constructor));
  }

  public void testGetConstructor() throws Exception {
    Constructor<String> constructor =
        String.class.getConstructor(char[].class, int.class, int.class);
    String result =
        ConstructorUtil.newInstance(constructor, new char[] {'a', 'b', 'c', 'd', 'e'}, 1, 3);
    assertEquals("bcd", result);
  }

  public class Hoge {
    public Hoge(String name) {}
  }
}
