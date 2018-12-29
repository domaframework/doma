package org.seasar.doma;

import junit.framework.TestCase;

public class DomaIllegalArgumentExceptionTest extends TestCase {

  public void test() throws Exception {
    DomaIllegalArgumentException e = new DomaIllegalArgumentException("aaa", "aaa < 0");
    assertEquals("aaa", e.getParameterName());
    assertEquals("aaa < 0", e.getDescription());
    System.out.println(e.getMessage());
  }
}
