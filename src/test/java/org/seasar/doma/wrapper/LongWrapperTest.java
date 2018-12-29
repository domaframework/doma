package org.seasar.doma.wrapper;

import junit.framework.TestCase;

public class LongWrapperTest extends TestCase {

  /** */
  public void testIncrement() {
    LongWrapper wrapper = new LongWrapper(10L);
    wrapper.increment();
    assertEquals(new Long(11L), wrapper.get());
  }

  /** */
  public void testDecrement() {
    LongWrapper wrapper = new LongWrapper(10L);
    wrapper.decrement();
    assertEquals(new Long(9L), wrapper.get());
  }
}
