package org.seasar.doma.jdbc.entity;

import junit.framework.TestCase;

public class OriginalStatesAccessExceptionTest extends TestCase {

  public void test() throws Exception {
    var e = new OriginalStatesAccessException(new Exception(), "aaa", "bbb");
    System.out.println(e.getMessage());
    assertEquals("aaa", e.getEntityClassName());
    assertEquals("bbb", e.getFieldName());
  }
}
