package org.seasar.doma.jdbc.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DomainTypeNotFoundExceptionTest {

  @Test
  public void test() throws Exception {
    DomainTypeNotFoundException e = new DomainTypeNotFoundException(new Exception(), "aaa", "bbb");
    System.out.println(e.getMessage());
    assertEquals("aaa", e.getDomainClassName());
    assertEquals("bbb", e.getDomainTypeClassName());
  }
}
