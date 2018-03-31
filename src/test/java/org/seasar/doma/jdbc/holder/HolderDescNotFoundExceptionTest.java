package org.seasar.doma.jdbc.holder;

import junit.framework.TestCase;

public class HolderDescNotFoundExceptionTest extends TestCase {

  public void test() throws Exception {
    HolderDescNotFoundException e = new HolderDescNotFoundException(new Exception(), "aaa", "bbb");
    System.out.println(e.getMessage());
    assertEquals("aaa", e.getHolderClassName());
    assertEquals("bbb", e.getHolderDescClassName());
  }
}
