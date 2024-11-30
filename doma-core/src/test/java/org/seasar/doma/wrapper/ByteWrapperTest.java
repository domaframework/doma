package org.seasar.doma.wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class ByteWrapperTest {

  @Test
  public void testSetNull() {
    ByteWrapper wrapper = new ByteWrapper();
    wrapper.set(null);
    assertNull(wrapper.get());
  }

  @Test
  public void testSetNullNumber() {
    ByteWrapper wrapper = new ByteWrapper();
    wrapper.set((Number) null);
    assertNull(wrapper.get());
  }

  /** */
  @Test
  public void testIncrement() {
    ByteWrapper wrapper = new ByteWrapper((byte) 10);
    wrapper.increment();
    assertEquals((byte) 11, wrapper.get());
  }

  /** */
  @Test
  public void testDecrement() {
    ByteWrapper wrapper = new ByteWrapper((byte) 10);
    wrapper.decrement();
    assertEquals((byte) 9, wrapper.get());
  }
}
