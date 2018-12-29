package org.seasar.doma.jdbc.entity;

import junit.framework.TestCase;

public class EntityTypeNotFoundExceptionTest extends TestCase {

  public void test() throws Exception {
    EntityTypeNotFoundException e = new EntityTypeNotFoundException(new Exception(), "aaa", "bbb");
    System.out.println(e.getMessage());
    assertEquals("aaa", e.getEntityClassName());
    assertEquals("bbb", e.getEntityTypeClassName());
  }
}
