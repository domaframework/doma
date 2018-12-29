package org.seasar.doma.wrapper;

import junit.framework.TestCase;

/** @author taedium */
public class FloatWrapperTest extends TestCase {

  /** */
  public void testIncrement() {
    FloatWrapper wrapper = new FloatWrapper(10f);
    wrapper.increment();
    assertEquals(new Float(11f), wrapper.get());
  }

  /** */
  public void testDecrement() {
    FloatWrapper wrapper = new FloatWrapper(10f);
    wrapper.decrement();
    assertEquals(new Float(9f), wrapper.get());
  }
}
