package org.seasar.doma;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DomaIllegalArgumentExceptionTest {

  @Test
  public void test() {
    DomaIllegalArgumentException e = new DomaIllegalArgumentException("aaa", "aaa < 0");
    assertEquals("aaa", e.getParameterName());
    assertEquals("aaa < 0", e.getDescription());
    System.out.println(e.getMessage());
  }
}
