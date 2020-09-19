package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ConfigExceptionTest {

  @Test
  public void test() {
    ConfigException e = new ConfigException("aaa", "bbb");
    System.out.println(e.getMessage());
    assertEquals("aaa", e.getClassName());
    assertEquals("bbb", e.getMethodName());
  }
}
