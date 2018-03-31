package org.seasar.doma.jdbc;

import junit.framework.TestCase;

public class SqlFileNotFoundExceptionTest extends TestCase {

  public void test() throws Exception {
    var e = new SqlFileNotFoundException("aaa");
    assertEquals("aaa", e.getPath());
    System.out.println(e.getMessage());
  }
}
