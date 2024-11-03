package org.seasar.doma.wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class FloatWrapperTest {

  @Test
  public void testSetNull() {
    FloatWrapper wrapper = new FloatWrapper();
    wrapper.set(null);
    assertNull(wrapper.get());
  }

  @Test
  public void testSetNullNumber() {
    FloatWrapper wrapper = new FloatWrapper();
    wrapper.set((Number) null);
    assertNull(wrapper.get());
  }

  /** */
  @Test
  public void testIncrement() {
    FloatWrapper wrapper = new FloatWrapper(10f);
    wrapper.increment();
    assertEquals(11f, wrapper.get());
  }

  /** */
  @Test
  public void testDecrement() {
    FloatWrapper wrapper = new FloatWrapper(10f);
    wrapper.decrement();
    assertEquals(9f, wrapper.get());
  }
}
