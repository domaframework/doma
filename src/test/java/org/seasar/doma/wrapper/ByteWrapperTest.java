package org.seasar.doma.wrapper;

import junit.framework.TestCase;

public class ByteWrapperTest extends TestCase {

  /** */
  public void testIncrement() {
    ByteWrapper wrapper = new ByteWrapper((byte) 10);
    wrapper.increment();
    assertEquals(Byte.valueOf((byte) 11), wrapper.get());
  }

  /** */
  public void testDecrement() {
    ByteWrapper wrapper = new ByteWrapper((byte) 10);
    wrapper.decrement();
    assertEquals(Byte.valueOf((byte) 9), wrapper.get());
  }
}
