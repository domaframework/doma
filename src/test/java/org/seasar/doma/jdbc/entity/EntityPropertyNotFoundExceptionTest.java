package org.seasar.doma.jdbc.entity;

import junit.framework.TestCase;

public class EntityPropertyNotFoundExceptionTest extends TestCase {

  public void test() throws Exception {
    var e = new EntityPropertyNotFoundException("aaa", "bbb");
    System.out.println(e.getMessage());
    assertEquals("aaa", e.getEntityClassName());
    assertEquals("bbb", e.getEntityPropertyName());
  }
}
