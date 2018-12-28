/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
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

  public void testTraverse() throws Exception {
    List<Class<?>> list = new ArrayList<>();
    ClassUtil.traverse(
        Ccc.class,
        c -> {
          list.add(c);
          return null;
        });
    assertEquals(8, list.size());
    assertEquals(Ccc.class, list.get(0));
    assertEquals(ICcc.class, list.get(1));
    assertEquals(Bbb.class, list.get(2));
    assertEquals(IBbb.class, list.get(3));
    assertEquals(IAaa.class, list.get(4));
    assertEquals(Aaa.class, list.get(5));
    assertEquals(IAaa.class, list.get(6));
    assertEquals(Object.class, list.get(7));
  }

  private interface IAaa {}

  private interface IBbb extends IAaa {}

  private interface ICcc {}

  private static class Aaa implements IAaa {}

  private static class Bbb extends Aaa implements IBbb {}

  private static class Ccc extends Bbb implements ICcc {}
}
