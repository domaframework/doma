package org.seasar.doma.wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ByteWrapperTest {

  /** */
  @Test
  public void testIncrement() {
    ByteWrapper wrapper = new ByteWrapper((byte) 10);
    wrapper.increment();
    assertEquals(new Byte((byte) 11), wrapper.get());
  }

  /** */
  @Test
  public void testDecrement() {
    ByteWrapper wrapper = new ByteWrapper((byte) 10);
    wrapper.decrement();
    assertEquals(new Byte((byte) 9), wrapper.get());
  }
}
