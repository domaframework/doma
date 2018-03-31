package org.seasar.doma.internal.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import junit.framework.TestCase;

/** @author taedium */
public class ClassUtilTest extends TestCase {

  public void testGetConstructor() throws Exception {
    Constructor<String> constructor =
        ClassUtil.getConstructor(String.class, char[].class, int.class, int.class);
    assertNotNull(constructor);
  }

  public void testGetDeclaredField() throws Exception {
    Field field = ClassUtil.getDeclaredField(TestCase.class, "fName");
    assertNotNull(field);
  }

  public void testGetSimpleName() throws Exception {
    String name = ClassUtil.getSimpleName("aaa.bbb.Ccc");
    assertEquals("Ccc", name);
  }

  public void testGetSimpleName_noPackage() throws Exception {
    String name = ClassUtil.getSimpleName("Aaa");
    assertEquals("Aaa", name);
  }

  public void testGetSimpleName_nestedClass() throws Exception {
    String name = ClassUtil.getSimpleName("aaa.bbb.Ccc$Ddd$Eee");
    assertEquals("Eee", name);
  }

  public void testGetEnclosingNames() {
    List<String> names = ClassUtil.getEnclosingNames("aaa.bbb.Ccc");
    assertTrue(names.isEmpty());
  }

  public void testGetEnclosingNames_noPackage() {
    List<String> names = ClassUtil.getEnclosingNames("Aaa");
    assertTrue(names.isEmpty());
  }

  public void testGetEnclosingNames_nestedClass() {
    List<String> names = ClassUtil.getEnclosingNames("aaa.bbb.Ccc$Ddd$Eee");
    assertEquals(2, names.size());
    assertEquals("Ccc", names.get(0));
    assertEquals("Ddd", names.get(1));
  }
}
