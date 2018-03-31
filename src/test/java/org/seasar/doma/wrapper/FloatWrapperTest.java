package org.seasar.doma.wrapper;

import junit.framework.TestCase;

public class FloatWrapperTest extends TestCase {

  /** */
  public void testIncrement() {
    FloatWrapper wrapper = new FloatWrapper(10f);
    wrapper.increment();
    assertEquals(Float.valueOf(11f), wrapper.get());
  }

  /** */
  public void testDecrement() {
    FloatWrapper wrapper = new FloatWrapper(10f);
    wrapper.decrement();
    assertEquals(Float.valueOf(9f), wrapper.get());
  }
}
