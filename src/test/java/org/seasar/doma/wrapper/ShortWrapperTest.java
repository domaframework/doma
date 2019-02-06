package org.seasar.doma.wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ShortWrapperTest {

  /** */
  @Test
  public void testIncrement() {
    ShortWrapper wrapper = new ShortWrapper((short) 10);
    wrapper.increment();
    assertEquals(new Short((short) 11), wrapper.get());
  }

  /** */
  @Test
  public void testDecrement() {
    ShortWrapper wrapper = new ShortWrapper((short) 10);
    wrapper.decrement();
    assertEquals(new Short((short) 9), wrapper.get());
  }
}
