package org.seasar.doma.jdbc.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EntityPropertyAccessExceptionTest {

  @Test
  public void test() {
    EntityPropertyAccessException e =
        new EntityPropertyAccessException(new Exception(), "aaa", "bbb");
    System.out.println(e.getMessage());
    assertEquals("aaa", e.getEntityClassName());
    assertEquals("bbb", e.getEntityPropertyName());
  }
}
