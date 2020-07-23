package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SqlFileNotFoundExceptionTest {

  @Test
  public void test() throws Exception {
    SqlFileNotFoundException e = new SqlFileNotFoundException("aaa");
    assertEquals("aaa", e.getPath());
    System.out.println(e.getMessage());
  }
}
