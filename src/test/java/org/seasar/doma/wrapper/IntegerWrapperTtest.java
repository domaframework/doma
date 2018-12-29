package org.seasar.doma.wrapper;

import junit.framework.TestCase;

public class IntegerWrapperTtest extends TestCase {

  /** */
  public void testIncrement() {
    IntegerWrapper wrapper = new IntegerWrapper(10);
    wrapper.increment();
    assertEquals(new Integer(11), wrapper.get());
  }

  /** */
  public void testDecrement() {
    IntegerWrapper wrapper = new IntegerWrapper(10);
    wrapper.decrement();
    assertEquals(new Integer(9), wrapper.get());
  }
}
