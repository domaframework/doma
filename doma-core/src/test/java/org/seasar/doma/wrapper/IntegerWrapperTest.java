package org.seasar.doma.wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class IntegerWrapperTest {

  @Test
  public void testSetNull() {
    IntegerWrapper wrapper = new IntegerWrapper();
    wrapper.set(null);
    assertNull(wrapper.get());
  }

  @Test
  public void testSetNullNumber() {
    IntegerWrapper wrapper = new IntegerWrapper();
    wrapper.set((Number) null);
    assertNull(wrapper.get());
  }

  /** */
  @Test
  public void testIncrement() {
    IntegerWrapper wrapper = new IntegerWrapper(10);
    wrapper.increment();
    assertEquals(11, wrapper.get());
  }

  /** */
  @Test
  public void testDecrement() {
    IntegerWrapper wrapper = new IntegerWrapper(10);
    wrapper.decrement();
    assertEquals(9, wrapper.get());
  }
}
