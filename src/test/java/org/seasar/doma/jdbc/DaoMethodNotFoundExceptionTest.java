package org.seasar.doma.jdbc;

import junit.framework.TestCase;

public class DaoMethodNotFoundExceptionTest extends TestCase {

  public void test() throws Exception {
    var cause = new Exception("hoge");
    var e = new DaoMethodNotFoundException(cause, "aaa", "bbb");
    System.out.println(e.getMessage());
    assertSame(cause, e.getCause());
    assertEquals("aaa", e.getClassName());
    assertEquals("bbb", e.getSignature());
  }
}
