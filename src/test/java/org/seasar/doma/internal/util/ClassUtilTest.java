package org.seasar.doma.internal.util;

import junit.framework.TestCase;

public class ClassUtilTest extends TestCase {

  public void testGetConstructor() throws Exception {
    var constructor = ClassUtil.getConstructor(String.class, char[].class, int.class, int.class);
    assertNotNull(constructor);
  }

  public void testGetDeclaredField() throws Exception {
    var field = ClassUtil.getDeclaredField(TestCase.class, "fName");
    assertNotNull(field);
  }

  public void testGetSimpleName() throws Exception {
    var name = ClassUtil.getSimpleName("aaa.bbb.Ccc");
    assertEquals("Ccc", name);
  }

  public void testGetSimpleName_noPackage() throws Exception {
    var name = ClassUtil.getSimpleName("Aaa");
    assertEquals("Aaa", name);
  }

  public void testGetSimpleName_nestedClass() throws Exception {
    var name = ClassUtil.getSimpleName("aaa.bbb.Ccc$Ddd$Eee");
    assertEquals("Eee", name);
  }

  public void testGetEnclosingNames() {
    var names = ClassUtil.getEnclosingNames("aaa.bbb.Ccc");
    assertTrue(names.isEmpty());
  }

  public void testGetEnclosingNames_noPackage() {
    var names = ClassUtil.getEnclosingNames("Aaa");
    assertTrue(names.isEmpty());
  }

  public void testGetEnclosingNames_nestedClass() {
    var names = ClassUtil.getEnclosingNames("aaa.bbb.Ccc$Ddd$Eee");
    assertEquals(2, names.size());
    assertEquals("Ccc", names.get(0));
    assertEquals("Ddd", names.get(1));
  }
}
