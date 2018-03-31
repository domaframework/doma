package org.seasar.doma.jdbc;

import junit.framework.TestCase;

/** @author taedium */
public class JdbcUnsupportedOperationExceptionTest extends TestCase {

  public void test() throws Exception {
    JdbcUnsupportedOperationException e = new JdbcUnsupportedOperationException("aaa", "bbb");
    System.out.println(e.getMessage());
    assertEquals("aaa", e.getClassName());
    assertEquals("bbb", e.getMethodName());
  }
}
