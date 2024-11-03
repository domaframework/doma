package org.seasar.doma.wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class ShortWrapperTest {

  @Test
  public void testSetNull() {
    ShortWrapper wrapper = new ShortWrapper();
    wrapper.set(null);
    assertNull(wrapper.get());
  }

  @Test
  public void testSetNullNumber() {
    ShortWrapper wrapper = new ShortWrapper();
    wrapper.set((Number) null);
    assertNull(wrapper.get());
  }

  /** */
  @Test
  public void testIncrement() {
    ShortWrapper wrapper = new ShortWrapper((short) 10);
    wrapper.increment();
    assertEquals((short) 11, wrapper.get());
  }

  /** */
  @Test
  public void testDecrement() {
    ShortWrapper wrapper = new ShortWrapper((short) 10);
    wrapper.decrement();
    assertEquals((short) 9, wrapper.get());
  }
}
