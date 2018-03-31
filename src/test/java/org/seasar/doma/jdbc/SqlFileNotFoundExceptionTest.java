package org.seasar.doma.jdbc;

import junit.framework.TestCase;

/** @author taedium */
public class SqlFileNotFoundExceptionTest extends TestCase {

  public void test() throws Exception {
    SqlFileNotFoundException e = new SqlFileNotFoundException("aaa");
    assertEquals("aaa", e.getPath());
    System.out.println(e.getMessage());
  }
}
