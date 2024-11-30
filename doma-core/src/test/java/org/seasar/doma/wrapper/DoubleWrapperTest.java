package org.seasar.doma.wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class DoubleWrapperTest {

  @Test
  public void testSetNull() {
    DoubleWrapper wrapper = new DoubleWrapper();
    wrapper.set(null);
    assertNull(wrapper.get());
  }

  @Test
  public void testSetNullNumber() {
    DoubleWrapper wrapper = new DoubleWrapper();
    wrapper.set((Number) null);
    assertNull(wrapper.get());
  }

  /** */
  @Test
  public void testIncrement() {
    DoubleWrapper wrapper = new DoubleWrapper(10d);
    wrapper.increment();
    assertEquals(11d, wrapper.get());
  }

  /** */
  @Test
  public void testDecrement() {
    DoubleWrapper wrapper = new DoubleWrapper(10d);
    wrapper.decrement();
    assertEquals(9d, wrapper.get());
  }
}
