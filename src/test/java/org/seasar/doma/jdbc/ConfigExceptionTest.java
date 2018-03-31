package org.seasar.doma.jdbc;

import junit.framework.TestCase;

public class ConfigExceptionTest extends TestCase {

  public void test() throws Exception {
    var e = new ConfigException("aaa", "bbb");
    System.out.println(e);
    assertEquals("aaa", e.getClassName());
    assertEquals("bbb", e.getMethodName());
  }
}
