package org.seasar.doma.jdbc.entity;

import junit.framework.TestCase;

public class EntityPropertyAccessExceptionTest extends TestCase {

  public void test() throws Exception {
    EntityPropertyAccessException e =
        new EntityPropertyAccessException(new Exception(), "aaa", "bbb");
    System.out.println(e.getMessage());
    assertEquals("aaa", e.getEntityClassName());
    assertEquals("bbb", e.getEntityPropertyName());
  }
}
