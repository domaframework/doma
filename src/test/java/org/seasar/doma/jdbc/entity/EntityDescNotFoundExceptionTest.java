package org.seasar.doma.jdbc.entity;

import junit.framework.TestCase;

public class EntityDescNotFoundExceptionTest extends TestCase {

  public void test() throws Exception {
    EntityDescNotFoundException e = new EntityDescNotFoundException(new Exception(), "aaa", "bbb");
    System.out.println(e.getMessage());
    assertEquals("aaa", e.getEntityClassName());
    assertEquals("bbb", e.getEntityDescClassName());
  }
}
