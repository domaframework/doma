package org.seasar.doma.jdbc.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EntityTypeNotFoundExceptionTest {

  @Test
  public void test() throws Exception {
    EntityTypeNotFoundException e = new EntityTypeNotFoundException(new Exception(), "aaa", "bbb");
    System.out.println(e.getMessage());
    assertEquals("aaa", e.getEntityClassName());
    assertEquals("bbb", e.getEntityTypeClassName());
  }
}
