package org.seasar.doma.wrapper;

import junit.framework.TestCase;

public class DoubleWrapperTest extends TestCase {

  /** */
  public void testIncrement() {
    DoubleWrapper wrapper = new DoubleWrapper(10d);
    wrapper.increment();
    assertEquals(new Double(11d), wrapper.get());
  }

  /** */
  public void testDecrement() {
    DoubleWrapper wrapper = new DoubleWrapper(10d);
    wrapper.decrement();
    assertEquals(new Double(9d), wrapper.get());
  }
}
