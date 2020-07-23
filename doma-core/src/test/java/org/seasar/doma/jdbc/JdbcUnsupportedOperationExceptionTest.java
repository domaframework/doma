package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class JdbcUnsupportedOperationExceptionTest {

  @Test
  public void test() throws Exception {
    JdbcUnsupportedOperationException e = new JdbcUnsupportedOperationException("aaa", "bbb");
    System.out.println(e.getMessage());
    assertEquals("aaa", e.getClassName());
    assertEquals("bbb", e.getMethodName());
  }
}
