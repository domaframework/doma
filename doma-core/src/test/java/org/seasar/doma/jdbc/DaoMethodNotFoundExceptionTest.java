package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class DaoMethodNotFoundExceptionTest {

  @Test
  public void test() {
    Exception cause = new Exception("hoge");
    DaoMethodNotFoundException e = new DaoMethodNotFoundException(cause, "aaa", "bbb");
    System.out.println(e.getMessage());
    assertSame(cause, e.getCause());
    assertEquals("aaa", e.getClassName());
    assertEquals("bbb", e.getSignature());
  }
}
