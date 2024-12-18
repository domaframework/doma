package org.seasar.doma.wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class LongWrapperTest {

  @Test
  public void testSetNull() {
    LongWrapper wrapper = new LongWrapper();
    wrapper.set(null);
    assertNull(wrapper.get());
  }

  @Test
  public void testSetNullNumber() {
    LongWrapper wrapper = new LongWrapper();
    wrapper.set((Number) null);
    assertNull(wrapper.get());
  }

  /** */
  @Test
  public void testIncrement() {
    LongWrapper wrapper = new LongWrapper(10L);
    wrapper.increment();
    assertEquals(11L, wrapper.get());
  }

  /** */
  @Test
  public void testDecrement() {
    LongWrapper wrapper = new LongWrapper(10L);
    wrapper.decrement();
    assertEquals(9L, wrapper.get());
  }
}
