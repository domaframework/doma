package org.seasar.doma.jdbc.domain;

import junit.framework.TestCase;

public class DomainTypeNotFoundExceptionTest extends TestCase {

  public void test() throws Exception {
    DomainTypeNotFoundException e = new DomainTypeNotFoundException(new Exception(), "aaa", "bbb");
    System.out.println(e.getMessage());
    assertEquals("aaa", e.getDomainClassName());
    assertEquals("bbb", e.getDomainTypeClassName());
  }
}
