package org.seasar.doma;

import junit.framework.TestCase;

/** @author taedium */
public class DomaNullPointerExceptionTest extends TestCase {

  public void test() throws Exception {
    DomaNullPointerException e = new DomaNullPointerException("aaa");
    assertEquals("aaa", e.getParameterName());
  }

  public void testGenerics() throws Exception {
    hoge(new A());
    hoge(new B());
    hoge(new C());
  }

  private <T> T hoge(T t) {
    return t;
  }

  private A hoge(A a) {
    return a;
  }

  static class A {}

  static class B extends A {}

  static class C {}
}
