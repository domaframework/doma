package org.seasar.doma.wrapper;

import junit.framework.TestCase;

public class IntegerWrapperTtest extends TestCase {

  /** */
  public void testIncrement() {
    var wrapper = new IntegerWrapper(10);
    wrapper.increment();
    assertEquals(Integer.valueOf(11), wrapper.get());
  }

  /** */
  public void testDecrement() {
    var wrapper = new IntegerWrapper(10);
    wrapper.decrement();
    assertEquals(Integer.valueOf(9), wrapper.get());
  }
}
