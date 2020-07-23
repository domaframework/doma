package org.seasar.doma.wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LongWrapperTest {

  /** */
  @Test
  public void testIncrement() {
    LongWrapper wrapper = new LongWrapper(10L);
    wrapper.increment();
    assertEquals(new Long(11L), wrapper.get());
  }

  /** */
  @Test
  public void testDecrement() {
    LongWrapper wrapper = new LongWrapper(10L);
    wrapper.decrement();
    assertEquals(new Long(9L), wrapper.get());
  }
}
