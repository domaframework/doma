package org.seasar.doma;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DomaNullPointerExceptionTest {

  @Test
  public void test() {
    DomaNullPointerException e = new DomaNullPointerException("aaa");
    assertEquals("aaa", e.getParameterName());
  }

  @Test
  public void testGenerics() {
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
