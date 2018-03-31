package org.seasar.doma.jdbc;

import junit.framework.TestCase;

public class JdbcUnsupportedOperationExceptionTest extends TestCase {

  public void test() throws Exception {
    var e = new JdbcUnsupportedOperationException("aaa", "bbb");
    System.out.println(e.getMessage());
    assertEquals("aaa", e.getClassName());
    assertEquals("bbb", e.getMethodName());
  }
}
