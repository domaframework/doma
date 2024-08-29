package org.seasar.doma.wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ShortWrapperTest {

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
