package org.seasar.doma.wrapper;

import junit.framework.TestCase;

public class ShortWrapperTest extends TestCase {

  /** */
  public void testIncrement() {
    var wrapper = new ShortWrapper((short) 10);
    wrapper.increment();
    assertEquals(Short.valueOf((short) 11), wrapper.get());
  }

  /** */
  public void testDecrement() {
    var wrapper = new ShortWrapper((short) 10);
    wrapper.decrement();
    assertEquals(Short.valueOf((short) 9), wrapper.get());
  }
}
